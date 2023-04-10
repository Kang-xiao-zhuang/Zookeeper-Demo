

# Zookeeper入门

Zookeeper 是一个开源的分布式的，为分布式框架提供协调服务的Apache 项目。

![](https://img-blog.csdnimg.cn/832f0324d5e2472a8cea9e8dfc35b81b.png)

## Zookeeper工作机制

Zookeeper从设计模式角度来理解：是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都关心的数据，然后接受观察者的注册，一旦这些数据的状态发生变化，Zookeeper就将负责通知已经在Zookeeper上注册的那些观察者做出相应的反应。



## Zookeeper特点

![](https://img-blog.csdnimg.cn/a827e5af09a945bd995203e9655051c9.png)

1）Zookeeper：一个领导者（Leader），多个跟随者（Follower）组成的集群。
2）集群中只要有半数以上节点存活，Zookeeper集群就能正常服务。所以Zookeeper适合安装奇数台服务器。
3）全局数据一致：每个Server保存一份相同的数据副本，Client无论连接到哪个Server，数据都是一致的。
4）更新请求顺序执行，来自同一个Client的更新请求按其发送顺序依次执行。
5）数据更新原子性，一次数据更新要么成功，要么失败。
6）实时性，在一定时间范围内，Client能读到最新数据。



## 数据结构

ZooKeeper 数据模型的结构与Unix 文件系统很类似，整体上可以看作是一棵树，每个节点称做一个ZNode。每一个ZNode 默认能够存储1MB 的数据，每个ZNode 都可以通过其路径唯一标识。



## 应用场景

提供的服务包括：统一命名服务、统一配置管理、统一集群管理、服务器节点动态上下线、软负载均衡等。

### 统一命名服务

在分布式环境下，经常需要对应用/服务进行统一命名，便于识别。

![](https://img-blog.csdnimg.cn/49dff600e7ca468ca9ff600a47a411e4.png)

### 统一配置管理

1. 分布式环境下，配置文件同步非常常见
    1. 一般要求一个集群中，所有节点的配置信息一致，比如Kafka集群
    2. 对配置文件修改，同步到各个节点
2. 配置管理交给Zookeeper实现
    1. 可将配置信息写入ZooKeeper上的一个Znode
    2. 各个客户端服务器监听这个Znode
    3. 一旦Znode中的数据被修改，ZooKeeper将通知各个客户端服务器

![](https://img-blog.csdnimg.cn/672a338641824b8caf04007b5ac81b40.png)

### 统一集群管理

1. 分布式环境中，实时掌握每个节点的状态是必要的
    1. 可根据节点实时状态做出一些调整
2. ZooKeeper可以实现实时监控节点状态变化
    1. 可将节点信息写入ZooKeeper上的一个ZNode
    2. 监听Znode可获取它的实时的状态变化

![](https://img-blog.csdnimg.cn/c9078e2fb3bd4e9bac59f8a227d55abf.png)

### 服务器动态上下线

**客户端能实时洞察到服务器上下线的变化**

![](https://img-blog.csdnimg.cn/27fb1fb21b59496aa83094a459fc75cf.png)

### 软负载均衡

**在Zookeeper中记录每台服务器的访问数，让访问数最少的服务器去处理最新的客户端请求**

![](https://img-blog.csdnimg.cn/8b1a57f3fd6540a3aad9470ec818ca1e.png)

## 下载安装



### 配置参数解读

Zookeeper中的配置文件zoo.cfg中参数含义解读如下：



tickTime = 2000：通信心跳时间，Zookeeper服务器与客户端心跳时间，单位毫秒

![](https://img-blog.csdnimg.cn/7d9c67c4df3948768f35f5639ebd42ea.png)



initLimit = 10：LF初始通信时限

![](https://img-blog.csdnimg.cn/def51e9966dd4af1b5c72eddbc414fc5.png)

> Leader和Follower初始连接时能容忍的最多心跳数（tickTime的数量）



syncLimit = 5：LF同步通信时限

![](https://img-blog.csdnimg.cn/def51e9966dd4af1b5c72eddbc414fc5.png)

> Leader和Follower之间通信时间如果超过syncLimit * tickTime，Leader认为Follwer死掉，从服务器列表中删除Follwer





dataDir：保存Zookeeper中的数据

> 注意：默认的tmp目录，容易被Linux系统定期删除，所以一般不用默认的tmp目录。



clientPort = 2181：客户端连接端口，通常不做修改。



## 客户端命令行操作

| 命令基本语法 | 功能描述                                                  |
| ------------ | --------------------------------------------------------- |
| help         | 显示所有操作命令                                          |
| ls path      | ls 查看当前znode的子节点 -w 监听子节点变化 -s附加次级信息 |
| create       | 普通创建 -s 含有序列 -e临时（重启或者超时消失）           |
| get path     | 获得节点的值可监听 -w 监听节点内容变化 -s 附加次级信息    |
| set          | 设置节点的具体值                                          |
| stat         | 查看节点状态                                              |
| delete       | 删除节点                                                  |
| deleteall    | 递归删除节点                                              |

### znode节点数据信息

**查看当前znode中所包含的内容**

```shell
[zk: hadoop102 :2181(CONNECTED) 0] ls
[zookeeper]
```

**查看当前节点详细数据**

```shell
[zk: hadoop102 :2181(CONNECTED) 5] ls s /
[zookeeper]cZxid = 0x0
ctime = Thu Jan 01 08:00:00 CST 1970
mZxid = 0x0
mtime = Thu Jan 01 08:00:00 CST 1970
pZxid = 0x0
cversion = 1
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 1
```

> （1 czxid 创建节点的事务 zxid
>
> 每次修改ZooKeeper状态都会 产生一个 ZooKeeper事务 ID。事务 ID是 ZooKeeper中所有修改总的次序。每 次 修改都有唯一的 zxid，如果 zxid1小于 zxid2，那么 zxid1在 zxid2之前发生。
> （2 ctime znode被创建的毫秒数（从 1970年开始）
> （3 mzxid znode最后更新的事务 zxid
> （4 mtime znode最后修改的毫秒数（从 1970年开始）
> （5 pZxid znode最后更新的子节点 zxid
>
> （6）cversion：znode 子节点变化号，znode 子节点修改次数
> （7）dataversion：znode 数据变化号
> （8）aclVersion：znode 访问控制列表的变化号
> （9）ephemeralOwner：如果是临时节点，这个是znode 拥有者的session id。如果不是
> 临时节点则是0。
> （10）dataLength：znode 的数据长度
> （11）numChildren：znode 子节点数量

### 节点类型（持久/短暂/有序号/无序号）

![](https://img-blog.csdnimg.cn/b0c1923a7cbf473f9ca56af9d877a61a.png)



### 监听器原理

客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、节点删除、子目录节点增加删除）时，ZooKeeper 会通知客户端。监听机制保证ZooKeeper 保存的任何的数据的任何改变都能快速的响应到监听了该节点的应用程序。

![](https://img-blog.csdnimg.cn/c3453a5661a3457ba1bec7e7f8080d73.png)

## 客户端API操作

添加pom文件

```xml
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.8.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.5.7</version>
        </dependency>

        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-client</artifactId>
            <version>4.3.0</version>
        </dependency>
    </dependencies>
```

需要在项目的`src/main/resources`目录下，新建一个文件，命名为“ `log4j.properties`”，在文件中填入。

```properties
log4j.rootLogger=INFO, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n
log4j.appender.logfile=org.apache.log4j.FileAppender
log4j.appender.logfile.File=target/spring.log
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n
```



```java
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
```

### 客户端向服务端写数据流程

**写流程之写入请求直接发送给Leader节点**

![](https://img-blog.csdnimg.cn/eb4de74938994dbfac5f6b79ef0a2a8d.png)

**写流程之写入请求发送给follower节点**

![](https://img-blog.csdnimg.cn/e07c18b4ad3844e494afb3c63439501f.png)

### 服务器动态上下线监听案例

某分布式系统中，主节点可以有多台，可以动态上下线，任意一台客户端都能实时感知到主节点服务器的上下线

![](https://img-blog.csdnimg.cn/2b5ec0de46ec40e69364c18ff6fb1a14.png)

先在集群上创建/servers 节点

```sh
create /servers "servers"
```

服务器端向Zookeeper注册代码

```java
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
	
    // 业务功能
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    // 注册服务器
    private void regist(String hostname) throws KeeperException, InterruptedException {
        zk.create("/servers/" + hostname, hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        log.info(hostname + " is online");
    }

    private void getConnect() throws IOException {
        zk = new ZooKeeper(CONNECTSTRING, SESSIONTIMEOUT, watchedEvent -> {
        });
    }
}
```

DistributeClient客户端代码

```java
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
```

## ZooKeeper分布式锁案例

什么叫做分布式锁呢？比如说"进程 1"在使用该资源的时候，会先去获得锁， ，"进程 1"获得锁以后会对该资源保持独占，这样其他进程就无法访问该资源，"进程1"用完该资源以后就将锁释放掉，让其他进程来获得锁，那么通过这个锁机制，我们就能保证了分布式系统中多个进程能够有序的访问该临界资源。那么我们把这个分布式环境下的这个锁叫作分布式锁。



![](https://img-blog.csdnimg.cn/e5f484a98e5b4243815bf392f1512110.png)

**分布式锁实现**

```java
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
```

**分布式锁测试**

(1)创建两个线程

```java
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
```

(2) 观察控制台变化

> 线程1获取锁
>
> 线程1释放锁
> 线程2获取锁
> 线程2释放锁



## Curator框架实现分布式锁案例

原生的 Java API开发存在的问题
（1) 会话连接是异步的，需要自己去处理。比如使用 CountDownLatch
（2) Watch需要重复注册，不然就不能生效
（3) 开发的复杂性还是比较高的
（4) 不支持多节点删除和创建。需要自己去递归



Curator是一个专门解决分布式锁的框架，解决了原生 Java API开发分布式遇到的问题。

详情请查看官方文档：
https://curator.apache.org/index.html



**Curator案例实操**

```xml
		<dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>4.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-client</artifactId>
            <version>4.3.0</version>
        </dependency>
```

**代码实现**

```java
package com.zhuang.zookeeper.case3;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

@Slf4j
public class CuratorLockTest {

    public static void main(String[] args) {

        // 创建分布式锁1
        InterProcessMutex lock1 = new InterProcessMutex(getCuratorFramework(), "/locks");

        // 创建分布式锁2
        InterProcessMutex lock2 = new InterProcessMutex(getCuratorFramework(), "/locks");

        new Thread(() -> {
            try {
                lock1.acquire();
                log.info("线程1 获取到锁");

                lock1.acquire();
                log.info("线程1 再次获取到锁");

                Thread.sleep(5000);

                lock1.release();
                log.info("线程1 释放锁");

                lock1.release();
                log.info("线程1  再次释放锁");

            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                lock2.acquire();
                log.info("线程2 获取到锁");

                lock2.acquire();
                log.info("线程2 再次获取到锁");

                Thread.sleep(5000);

                lock2.release();
                log.info("线程2 释放锁");

                lock2.release();
                log.info("线程2  再次释放锁");

            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private static CuratorFramework getCuratorFramework() {

        ExponentialBackoffRetry policy = new ExponentialBackoffRetry(3000, 3);

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(2000)
                .retryPolicy(policy).build();

        // 启动客户端
        client.start();

        log.info("zookeeper 启动成功");
        return client;
    }
}
```

> 线程1获取锁
> 线程1再次获取锁
> 线程1释放锁
> 线程1再次释放锁
> 线程2获取锁
> 线程2再次获取锁
> 线程2释放锁
> 线程2再次释放锁



## 企业面试真题（面试重点）

### 选举机制

半数 机制 ，超过半数的投票通过，即通过。
（1）第一次启动选举规则
投票过半数时，
服务器 id大的胜出
（2）第二次启动选举规则
①EPOCH大的直接胜出
②EPOCH相同，事务 id大的胜出
③事务id相同，服务器 id大的胜出

### 生产集群安装多少zk合适

安装奇数台
生产经验：

- 10台 服务器： 3台 zk
- 20台 服务器： 5台 zk
- 100台 服务器： 11台 zk
- 200台 服务器： 11台 zk

服务器台数多：好处，提高可靠性；坏处：提高通信延时

### 常用命令

ls、 get、 create、 delete

## SpringBoot整合Zookeeper

采用的版本`apache-zookeeper-3.8.0-bin`

![](https://img-blog.csdnimg.cn/0656c12cd00d455889bc649631077b4d.png)



创建SpringBoot项目导入依赖

```xml
<properties>
        <java.version>8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.6.3</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--Curator-->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>5.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>5.2.1</version>
        </dependency>
        <!--解决Spring Boot Configuration Annotation Processor not configured提示问题-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

配置`application.yml`

```yaml
curator:
  #重试retryCount次，当会话超时出现后，curator会每间隔elapsedTimeMs毫秒时间重试一次，共重试retryCount次。
  retryCount: 5
  elapsedTimeMs: 5000
  #服务器信息
  connectString: 127.0.0.1:2181
  #会话超时时间设置
  sessionTimeoutMs: 60000
  #连接超时时间
  connectionTimeoutMs: 5000

server:
  port: 80
```

Zookeeper配置类

```java
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
```

**CuratorConfig**

```java
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
```

**创建Controller通过Postman测试**

```java
@RestController
@Slf4j
public class ZookeeperController {

    @Autowired
    private CuratorFramework curatorFramework;
    
}
```

### 创建节点

```java
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

        return "create" + nodeId + "successfully!";
    }
```

![](https://img-blog.csdnimg.cn/3320a68fd4504149a959ecd90c413577.png)

![](https://img-blog.csdnimg.cn/cd64ee0c35084872b50945823740a802.png)



### 获取指定节点的值

```java
    /**
     * 获取指定节点的值
     */
    @GetMapping("/getNode/{nodeId}")
    public String getNode(@PathVariable String nodeId) throws Exception {

        byte[] bytes = curatorFramework.getData().forPath("/" + nodeId);
        log.info(new String(bytes, StandardCharsets.UTF_8));
        return new String(bytes, StandardCharsets.UTF_8);

    }
```

![](https://img-blog.csdnimg.cn/57c1443102c2437480b2fd8d6404dc29.png)



### 获取所有节点

```java
    /**
     * 获取所有节点
     */
    @GetMapping("/getAllNode")
    public List<String> getAllData() throws Exception {

        return curatorFramework.getChildren().forPath("/");

    }
```

![](https://img-blog.csdnimg.cn/67e3f571c313440ea77931799e6923a4.png)



### 修改节点数据

```java
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
```

![](https://img-blog.csdnimg.cn/f64cdd3d5d7a4eff8217fdd86a004d4f.png)



### 创建节点同时创建父节点

```java
    /**
     * 创建节点同时创建父节点
     */
    @PostMapping("/createNode/{parentNodeId}/{childNodeId}")
    public String createWithParent(@PathVariable String parentNodeId, @PathVariable String childNodeId) throws Exception {

        String pathWithParent = "/" + parentNodeId + "/" + childNodeId;

        String path = curatorFramework.create().creatingParentsIfNeeded().forPath(pathWithParent);

        return "create node " + path + " successfully!!!";
    }
```

![](https://img-blog.csdnimg.cn/7733d1070a9a49fb9a9834f677701edd.png)

![](https://img-blog.csdnimg.cn/8a82bba4ed1f49fc8ca46684e5fb997f.png)

### 删除节点(包括子节点)

```java
    /**
     * 删除节点(包含子节点)
     */
    @DeleteMapping("/deleteNode/{nodeId}")
    public String deleteNode(@PathVariable String nodeId) throws Exception {
        String pathWithParent = "/" + nodeId;
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(pathWithParent);
        return "delete " + nodeId + " successfully";
    }
```

![](https://img-blog.csdnimg.cn/e058f6dfc692451387c2e0900c117128.png)
![](https://img-blog.csdnimg.cn/4e5030cacd214433bf45302bf1807f20.png)
