## 直连提供者
在开发以及测试环境下，经常需要绕过注册中心，只是测试指定服务器提供者，这个时候可能需要点对点直连， 点对点直连方式，
将以服务接口为单元，忽略注册中心的提供者列表，A接口配置点对点，不影响B接口从注册中心获取列表
1. 如果是线上需求要求点对点，可以在<dubbo:reference>中配置，url指向提供者，将绕过注册中心，多个地址用分号隔开
```xml
<dubbo:reference id ="xxxService" interface="com.alibaba.xxx.XxxService" url="dubbo://localhost2818"/>
```
2. 在JVM启动参数中加入-D参数映射服务地址如（key 为服务者， value为服务提供者URL， ）
```cmd
java -Dcom.alibaba.xxx.XxxService=dubbo://localhost:28181
```
3. 如果服务比较多，可以用文件映射如（用-Ddubbo.resolve.file指定映射文件路径，此配置优先级高于<dubbo:reference>中的配置），自动
加载${user.home}/dubbo-resolve.properties(2.0以上版本)
```cmd
java -Ddubbo.reolvefile=xx.properties
```
然后映射文件xxx.properties加入 （key为服务名，value为服务提供者url）
com.alibaba.xxx.XxxService=dubbo://localhost:28383

## 注意点
1. 直连提供者只需要在消费端配置
2. ${user.home}指的是当前操作系统用户目录，如win7系统 Administrator的用户目录 C:\Users\Administrator