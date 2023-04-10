package com.zhuang.zookeeper.case2;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class DistributedLock {

    // zookeeper server 列表
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    // 超时时间
    private static final int SESSIONTIMEOUT = 2000;
    private final ZooKeeper zk;

    private final CountDownLatch connectLatch = new CountDownLatch(1);
    private final CountDownLatch waitLatch = new CountDownLatch(1);

    // 当前client等待的子节点
    private String waitPath;
    // 当前client创建的子节点
    private String currentMode;

    private String rootNode = "locks";
    private String subNode = "seq-";

    // 和 zk 服务建立连接，并创建根节点
    public DistributedLock() throws IOException, InterruptedException, KeeperException {

        // 获取连接
        zk = new ZooKeeper(CONNECTSTRING, SESSIONTIMEOUT, watchedEvent -> {
            // connectLatch  如果连接上zk  可以释放
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectLatch.countDown();
            }

            // waitLatch  需要释放
            if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)) {
                waitLatch.countDown();
            }
        });

        // 等待zk正常连接后，往下走程序
        connectLatch.await();

        // 判断根节点/locks是否存在
        Stat stat = zk.exists("/" + rootNode, false);

        // 如果根节点不存在，则创建根节点，根节点类型为永久节点
        if (stat == null) {
            log.info("根节点不存在！！！");
            // 创建一下根节点
            zk.create("/" + rootNode, rootNode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    // 对zk加锁
    public void zklock() {
        // 创建对应的临时带序号节点
        try {
            currentMode = zk.create("/" + rootNode + "/" + subNode, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // wait一小会, 让结果更清晰一些
            Thread.sleep(10);

            // 判断创建的节点是否是最小的序号节点，如果是获取到锁；如果不是，监听他序号前一个节点

            List<String> children = zk.getChildren("/locks", false);

            // 如果children 只有一个值，那就直接获取锁； 如果有多个节点，需要判断，谁最小
            if (children.size() == 1) {
                return;
            } else {
                Collections.sort(children);

                // 获取节点名称 seq-00000000
                String thisNode = currentMode.substring("/locks/".length());
                // 通过seq-00000000获取该节点在children集合的位置
                int index = children.indexOf(thisNode);

                // 判断
                if (index == -1) {
                    log.error("数据异常");
                } else if (index == 0) {
                    // 就一个节点，可以获取锁了
                    return;
                } else {
                    // 需要监听  他前一个节点变化
                    waitPath = "/locks/" + children.get(index - 1);
                    zk.getData(waitPath, true, new Stat());

                    // 等待监听
                    waitLatch.await();
                    return;
                }
            }
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    // 解锁
    public void unZkLock() {
        // 删除节点
        try {
            zk.delete(this.currentMode, -1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
