> 其实就是如何将一个新节点加入集群中，或者从集群中删除节点

## 1、安装新的Redis 节点、将用于扩展性测试
1. 在192.168.1.117虚拟机上以同样的方式安装Redis3， 并启动两个实例，规划如下
2. Redis安装过程
3. 创建集群配置目录，并拷贝redis.conf配置文件到各节点配置目录。
4. 在192.168.1.117上使用如下命令启动这个2个Redis实例。

## 2、Redis集群的扩展性测试
1. redis-trib.rb命令介绍:
- call: 执行Redis命令
- create： 创建一个新的集群
- add-node: 将一个节点添加到集群里面，第一个新节点,ip:port, 第二个是集群中任意一个正常节点 ip:port, --master-id
- reshard: 重新分片
- check : 查看集群信息
- del-node: 移除一个节点
2. 添加新的Master节点：

add-node将一个节点添加到集群里面，第一个是新节点ip:port, 第二个是任意一个已存在节点 ip:port

以上操作结果表示节点添加成功，新增的节点不包含任何数据，因为他没有分配任何slot， 新加入的节点是一个master节点，当集群需要将某个
从节点升级为新的主节点时，这个新及诶单不会被选中。为新节点分配哈希槽（slot）
```cmd
# /usr/local/src/redis-3.0.3/src/redis-trib.rb reshard 192.168.1.111:7111

注意：可以同步观察重新分片是否会对客户端的连续使用产生影响（结果：不影响）

在重新分片操作操作执行完毕后，可以使用一下命令来检查集群是否正常
# /usr/local/src/redis-3.0.3/src/redis-trib.rb check 192.168.1.111:7111
```
3. 添加新的slave节点
```cmd
# /usr/local/src/redis-3.0.3/src/redis-trib.rb add-node 192.168.1.117:7118 192.168.1.111:7111

# redis-cli 连接上新节点shell 输入命令：cluster replicate 对应master的node-id
# /usr/local/redis3/bin/redis-cli -c -p 7118

127.0.0.1:7118> cluster replicate ab23232242sdafwearer334343f
```
4. 在线reshard数据（对于负载不均衡问题，重新分片）
5. 删除一个slave节点
```cmd
# /usr/local/src/redis-3.0.3/src/redis-trib.rb del-node 192.168.1.117:7118  dadf33432322222saafe3322

```
6. 删除一个master节点
删除master节点之前首先使用reshard移除该master的全部slot，然后在删除当前节点（目前只能把被删除master的slot迁移到一个节点上）
操作和分配slot类似，指定具体的source code即可。

```cmd
# /usr/local/src/redis-3.0.3/src/redis-trib.rb reshard 192.168.1.117:7117
```
确认已清空该Master节点的所有slot后就可以删除该节点了（命令与删除slave节点一样）