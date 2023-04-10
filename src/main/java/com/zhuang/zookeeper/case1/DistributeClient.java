package com.zhuang.zookeeper.case1;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器动态上下线客户端
 */
@Slf4j
public class DistributeClient {

    private static final String CONNECTSTRING = "127.0.0.1:2181";
    private static final int SESSIONTIMEOUT = 2000;
    private ZooKeeper zk;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeClient client = new DistributeClient();

        // 1 获取zk连接
        client.getConnect();

        // 2 监听/servers下面子节点的增加和删除
        client.getServerList();

        // 3 业务逻辑（睡觉）
        client.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Integer.MAX_VALUE);
    }

    // 获取服务器列表信息
    private void getServerList() throws KeeperException, InterruptedException {

        // 1 获取服务器 子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren("/servers", true);

        // 2 存储服务器信息列表
        ArrayList<String> servers = new ArrayList<>();

        // 3 遍历所有节点，获取节点中的主机名称信息
        for (String child : children) {

            byte[] data = zk.getData("/servers/" + child, false, null);

            servers.add(new String(data));
        }
        // 打印
        log.info(String.valueOf(servers));
    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(CONNECTSTRING, SESSIONTIMEOUT, watchedEvent -> {
            try {
                getServerList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}