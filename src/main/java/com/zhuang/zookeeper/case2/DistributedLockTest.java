package com.zhuang.zookeeper.case2;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

@Slf4j
public class DistributedLockTest {

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {

        final DistributedLock lock1 = new DistributedLock();

        final DistributedLock lock2 = new DistributedLock();

        new Thread(() -> {
            try {
                lock1.zklock();
                log.info("线程1 启动，获取到锁");
                Thread.sleep(5000);

                lock1.unZkLock();
                log.info("线程1 释放锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {

            try {
                lock2.zklock();
                log.info("线程2 启动，获取到锁");
                Thread.sleep(5000);

                lock2.unZkLock();
                log.info("线程2 释放锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();

    }
}
