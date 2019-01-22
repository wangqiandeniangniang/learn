## 负载均衡
> 在集群负载均衡时，Dubbo提供了多种均衡策略，缺省为random随机调用

1. Random LoadBalance
    - 随机，按权重设置随机概率
    - 在一个截面上碰撞的概率高，但是调用量越大分布越均匀，而且按概率使用权重也比较均匀，有利于动态调整提供者的权重
2. RoundRobin LoadBalance
    - 轮询， 按公约的权重设置轮询的比率
    - 存在慢的提供者累计请求问题，比如：第二台机器很慢，但没有挂，当请求调用第二台时就卡在那，久而久之，所有请求都卡在调用第二台服务器
3. LeastActive LoadBalance
    - 最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差
    - 使用慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大
4. ConsistentHash LoadBalance
    - 一致性hash,相同参数的请求总是发到同一提供者
    - 当某台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其他提供者，不会引起剧烈变动。
    - 缺省只对对一个参数Hash，如果要修改，请求配置<dubbo:parameter key="hash.arguments" value="0.1">
    - 缺省用160份虚拟节点，如果要修改，请求配置<dubbo:parameter key="hash.nodes" value="320"/>
    
## 例如配置
```xml
<dubbo:service interface="...." loadbalance="roundrobin"/>

<dubbo:reference interface="..." loadbalance="roundrobin"/>

<dubbo:service interface="....">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>

<dubbo:service interface="....">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>

```
    
## 负载均衡扩展

```java
com.alibaba.dubbo.rpc.cluster.LoadBalance
```

扩展配置：
<dubbo:protocol loadbalance="xxx"/>
<dubbo:provider loadbalance="xxx"/>