package com.zhuang.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootTest
class SpringBootZookeeperApplicationTests {

    @Autowired
    private CuratorFramework curatorFramework;


    @Test
    void contextLoads() {
    }

    /**
     * 创建节点
     */
    @Test
    void createNode() throws Exception {
        // 添加持久节点
        String path = curatorFramework.create().forPath("/curator-node");
        System.out.printf("curator create node :%s successfully.%n", path);

        // 添加临时序号节点,并赋值数据
        curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/curator-node", "some-datakx".getBytes());
        System.out.printf("curator create node :%s successfully.%n", path);

        byte[] bytes = curatorFramework.getData().forPath("/curator-node");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        // System.in.read()目的是阻塞客户端关闭，我们可以在这期间查看zk的临时序号节点
        // 当程序结束时候也就是客户端关闭的时候，临时序号节点会消失
        System.in.read();
    }

    /**
     * 获取节点
     */
    @Test
    void testGetData() throws Exception {
        // 在上面的方法执行后，创建了curator-node节点，但是我们并没有显示的去赋值
        // 通过这个方法去获取节点的值会发现，当我们通过Java客户端创建节点不赋值的话默认就是存储的创建节点的ip
        byte[] bytes = curatorFramework.getData().forPath("/curator-node");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * 修改节点数据
     */
    @Test
    void testSetData() throws Exception {
        // 两个方法都可以实现修改节点数据，如果存在节点就修改值，不存在就创建
        curatorFramework.create().orSetData().forPath("/curator-node", "康小庄!6666".getBytes());
        //curatorFramework.setData().forPath("/curator-node", "康小庄!".getBytes());
        byte[] bytes = curatorFramework.getData().forPath("/curator-node");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * 创建节点同时创建⽗节点
     */
    @Test
    void testCreateWithParent() throws Exception {
        String pathWithParent = "/node-parent/sub-node-1";
        String path = curatorFramework.create().creatingParentsIfNeeded().forPath(pathWithParent);
        System.out.printf("curator create node :%s successfully.%n", path);
    }

    /**
     * 删除节点(包含子节点)
     */
    @Test
    void testDelete() throws Exception {
        String pathWithParent = "/node-parent";
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
    }

}
