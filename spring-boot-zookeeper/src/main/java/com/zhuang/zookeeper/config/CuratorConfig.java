package com.zhuang.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description: CuratorConfig
 * date: 2023/4/9 22:07
 * author: Zhuang
 * version: 1.0
 */
@Configuration
public class CuratorConfig {


    @Autowired
    private ZkConfig zkConfig;

    /**
     * 这里的start就是创建完对象放到容器后，需要调用他的start方法
     *
     * @return CuratorFramework
     */
    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                zkConfig.getConnectString(),
                zkConfig.getSessionTimeoutMs(),
                zkConfig.getConnectionTimeoutMs(),
                new RetryNTimes(zkConfig.getRetryCount(), zkConfig.getElapsedTimeMs()));
    }

}
