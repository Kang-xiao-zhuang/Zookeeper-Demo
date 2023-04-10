package com.zhuang.zookeeper.case1;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import java.io.IOException;

/**
 * 服务器动态上下线服务端
 */
@Slf4j
public class DistributeServer {

    private static final String CONNECTSTRING = "127.0.0.1:2181";
    private static final int SESSIONTIMEOUT = 2000;
    private ZooKeeper zk;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        DistributeServer server = new DistributeServer();
        // 1 获取zk连接
        server.getConnect();

        // 2 注册服务器到zk集群
        server.regist("hadoop101");

        // 3 启动业务逻辑（睡觉）
        server.business();

    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void regist(String hostname) throws KeeperException, InterruptedException {
        zk.create("/servers/" + hostname, hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        log.info(hostname + " is online");
    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(CONNECTSTRING, SESSIONTIMEOUT, watchedEvent -> {
        });
    }
}
