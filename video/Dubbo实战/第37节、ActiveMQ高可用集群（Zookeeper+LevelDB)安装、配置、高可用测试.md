## 1、ActiveMQ集群方式
总共有三种方式
- Zookeeper + LevelDB的Master-Slave实现方式

LevelDB 是 Google开发的、持久化、高性能、类库， 单进程、 十亿key-value数据，内存小

集群仅提供主备方式的高可用集群功能，避免单点故障，没有负载均衡功能。

原理： 就是备用轮胎一样，只有在轮胎坏就换上
- 目录共享 （KahaDB，默认）
- 数据库共享 （基于JDBC）

## 2、ActiveMQ配置  （conf/activemq.xml)
- 管控台端口： 修改 bean id="jettyPort"
- 集群配置端口：主要用于选举用的 修改broker标签， 注意brokerName的值必须相同
- 消息端口：transportConnector: name="openwire" 需要修改端口

## 3、启动ActiveMQ节点和监听日志
## 4、集群的节点状态分析：使用ZooInspector

## 5、集群可用性测试（配置和测试代码）
ActiveMQ的客户端只能访问Master的Broker，其他处于Slave的Broker不能访问，所以客户端连接Broker应该使用failover
```properties
failover:(tcp://192.162.1.81:51511,tcp://192.162.1.81:51512,tcp://192.162.1.81:51513)?randomize=false
```

## 6、集群高可用测试
ActiveMQ集群的高可用，依赖于Zookeeper集群的高可用。

## 7、设置开机启动
```cmd
vi /etc/rc.local
```

## 8、配置优化
从文本中读取ip+port
```properties
failover:()?randomize=false&updateURIsURL=file:/home/wusc/activemq/urllist.txt

urllist.txt中地址通过英文逗号分隔，示例
tcp://192.162.1.81:51511,tcp://192.162.1.81:51512,tcp://192.162.1.81:51513
```
## 9、警告
replicatedLevelDB不支持延迟或计划任务消息。不能复制到slave Broker上，不能实现消息的高可用。