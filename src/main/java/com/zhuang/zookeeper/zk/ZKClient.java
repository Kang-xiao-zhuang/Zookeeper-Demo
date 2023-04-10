package com.zhuang.zookeeper.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ZKClient {

    // 注意：逗号左右不能有空格
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    private static final int SESSIONTIMEOUT = 2000;
    private ZooKeeper zookeeper;

    // 创建 ZooKeeper客户端
    @Before
    public void init() throws IOException {

        zookeeper = new ZooKeeper(CONNECTSTRING, SESSIONTIMEOUT, watchedEvent -> {
            log.info("---------------------------------");
            List<String> children;
            try {
                children = zookeeper.getChildren("/", true);

                for (String child : children) {
                    log.info(child);
                }
                log.info("---------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 创建子节点
    @Test
    public void create() throws KeeperException, InterruptedException {
        zookeeper.create("/kangxiaozhuang", "ss.avi".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // 获取子节点并监听节点变化
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zookeeper.getChildren("/", true);

        for (String child : children) {
            log.info(child);
        }
        // 延时
        Thread.sleep(5000);
    }

    // 节点是否存在
    @Test
    public void exist() throws KeeperException, InterruptedException {

        Stat stat = zookeeper.exists("/atguigu", false);

        log.info(stat == null ? "not exist " : "exist");
    }
}
