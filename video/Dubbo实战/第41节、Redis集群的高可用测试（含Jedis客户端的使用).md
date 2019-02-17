## 1、Jedis的客户端代码
- 数据库连接池配置
- Redis集群的节点集合
- 节点、超时时间、最多重定向次数、连接池

## 1.1、Spring配置Jedis链接Redis3.0集群的配置：
- 配置 redis.clients.jedis.JedisPoolConfig
- 配置 redis.clients.jedis.JedisCluster

## 2、Redis集群的高可用性测试
### 2.1、Redis集群特点
1. 集群架构特点：
- 所有的redis节点彼此互联（PING-PONG机制），内部使用二进制协议优化传输速度和带宽
- 节点的fail是通过集群中超过半数的节点检测失效时才生效
- 客户端与redis节点直接、不需要中间proxy层，客户不需要连接集群所有节点，连接集群任何一个可用节点即可
- Redis-cluster把所有的物理节点映射到【0-16383】个slot哈希槽上、cluster负债维护node<->slot<->value.

2. 集群选举容错：
- 节点失效选举过程是集群中所有master参与，如果半数以上master节点与当前被检测master节点通信检测超时（cluster-node-timeout),
就认为当前master节点挂掉：
- 什么时候整个集群不可用（cluster_state:fail)?
    - 如果 集群任意master挂掉，且当前master没有slave，集群进入fail状态，也可以理解成集群的slot映射[0-16383]不完整时进入fail状态，
    ps： redis-3.0.0.rcl 加入 cluster-require-full-coverage参数，默认关闭，打开集群兼容部分失败
    - 如果集群超过半数以上master挂点，无论是否有slave集群进入fail状态，
    ps:当集群不可用时，所有对集群的操作都不可用，收到（error） CLUSTERDOWN the cluster is down） 错误。