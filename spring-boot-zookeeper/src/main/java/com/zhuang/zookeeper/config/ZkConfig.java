package com.zhuang.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description: ZkConfig
 * date: 2023/4/9 22:06
 * author: Zhuang
 * version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "curator")
public class ZkConfig {

    private int retryCount;
    private int elapsedTimeMs;
    private String connectString;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;

}
