package com.zhuang.zookeeper.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * description: ZookeeperController
 * date: 2023/4/9 22:34
 * author: Zhuang
 * version: 1.0
 */
@RestController
@Slf4j
public class ZookeeperController {

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 创建节点
     */
    @PostMapping("/create/{nodeId}/{data}")
    public String createNode(@PathVariable String nodeId, @PathVariable String data) throws Exception {

        // 添加持久节点
        String path = curatorFramework.create().forPath("/" + nodeId);
        log.info("{} node :{} successfully!!!", nodeId, path);
        // 添加临时序号节点,并赋值数据
        curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/" + nodeId, data.getBytes());
        log.info("{} node :{} successfully!!!", nodeId, path);

        byte[] bytes = curatorFramework.getData().forPath("/" + nodeId);
        log.info(new String(bytes, StandardCharsets.UTF_8));

        return "create " + nodeId + " successfully!";
    }


    /**
     * 获取指定节点的值
     */
    @GetMapping("/getNode/{nodeId}")
    public String getNode(@PathVariable String nodeId) throws Exception {

        byte[] bytes = curatorFramework.getData().forPath("/" + nodeId);
        log.info(new String(bytes, StandardCharsets.UTF_8));
        return new String(bytes, StandardCharsets.UTF_8);

    }


    /**
     * 获取所有节点
     */
    @GetMapping("/getAllNode")
    public List<String> getAllData() throws Exception {

        return curatorFramework.getChildren().forPath("/");

    }


    /**
     * 修改节点数据
     */
    @PutMapping("/updateData/{nodeId}/{newData}")
    public String testSetData(@PathVariable String nodeId, @PathVariable String newData) throws Exception {

        byte[] b1 = curatorFramework.getData().forPath("/" + nodeId);

        // 两个方法都可以实现修改节点数据，如果存在节点就修改值，不存在就创建
        curatorFramework.create().orSetData().forPath("/" + nodeId, newData.getBytes());
        //curatorFramework.setData().forPath("/curator-node", "康小庄!".getBytes());

        byte[] b2 = curatorFramework.getData().forPath("/" + nodeId);

        return "修改前" + nodeId + "节点的值" + new String(b1, StandardCharsets.UTF_8) + ",修改为->" + new String(b2, StandardCharsets.UTF_8);
    }


    /**
     * 创建节点同时创建父节点
     */
    @PostMapping("/createNode/{parentNodeId}/{childNodeId}")
    public String createWithParent(@PathVariable String parentNodeId, @PathVariable String childNodeId) throws Exception {

        String pathWithParent = "/" + parentNodeId + "/" + childNodeId;

        String path = curatorFramework.create().creatingParentsIfNeeded().forPath(pathWithParent);

        return "create node " + path + " successfully!!!";
    }

    /**
     * 删除节点(包含子节点)
     */
    @DeleteMapping("/deleteNode/{nodeId}")
    public String deleteNode(@PathVariable String nodeId) throws Exception {
        String pathWithParent = "/" + nodeId;
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
        return "delete " + nodeId + " successfully";
    }
}
