## 1、主要配置

集群1链接集群2：
```xml
<networkConnectors>
    <networkConnector uri="static:(tcp://192.168.1.101.53531,tcp://192.168.1.101.53532,tcp://192.168.1.101.53533)"
        duplex="false"></networkConnector>
</networkConnectors>

```

集群2链接集群1
```xml
<networkConnectors>
    <networkConnector uri="static:(tcp://192.168.1.81.51511,tcp://192.168.1.82.51512,tcp://192.168.1.83.51513)"
        duplex="false"></networkConnector>
</networkConnectors>

```

两个消息集群的消息可以互相联通进行消费