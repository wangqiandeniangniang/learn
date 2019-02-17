## 1、参考文档
![官方集群指南](https://redis.io/topics/cluster-tutorial)
![官方集群指南翻译](http://redisdoc.com/topic/cluster-tutorial.html)

## 2、Redis集群的安装 3个master节点、3个slave节点
两种端口：服务端口和集群端口

关系： 集群端口=服务端口数+10000

## 3、打开端口的防火墙
下载或上传redis到服务器上，当然也要安装 gcc和 tcl工具用于编译和安装redis
```cmd
安装（使用PREFIX指定安装目录）：
# make PREFIX=/usr/local/redis3 install
安装完成后，可以看到/usr/local/redis3目录下有一个bin目录，bin目录里就是redis的命令脚本
```

## 4、创建集群配置目录，并拷贝redis.conf配置文件到各个节点配置目录。


## 5、用ruby创建redis集群
```cmd
安装ruby和rubygems（注意：需要ruby的版本在1.8.7以上）
# yum install ruby rubygems

检查ruby版本
# ruby -v

gem 安装redis ruby 接口
# gem install redis


执行Redis集群创建命令（只需要在其中一个节点上执行一次即可）
# cd /usr/local/src/redis-3.0.3/src/
# cp redis-trib.rb /usr/local/bin/redis-trib
### --replicas 1 表示只有一个从节点，  前三个ip+端口表示salve， 后三个ip+端口表示master
# redis-trib create --replicas 1 192.168.1.114:7114 192.168.1.115:7115 192.168.1.116:7116 
192.168.1.111:7111 192.168.1.112:7112 192.168.1.113:7113
```
## 6、将Redis配置成服务
```cmd
./redis-cli -p 7111 cluster nodes    // 7111的集群节点状态
```
- 按上面的操作步骤，Redis的启动脚本为：/usr/local/redis-3.0.3/utils/redis_init_script, 将启动脚本复制到/etc/rc.d/init.d/
目录下，并命名为redis
    - 在脚本的第一行后面添加一行内容如下：
    #chkconfig: 2345 80 90 
    - REDISPORT 端口修改各个节点对应的端口
    - EXEC=/usr/local/bin/redis-server  redis服务路径
    - CLIEXEC=/usr/local/bin/redis-cli  redis 客户端
    - 配置文件设置，自动生成
    - 更改redis开启的命令，以后台运行的方式执行： $EXEC $CONF &      # "&"作用是将服务转到后面运行
- 以上配置操作完成后，便可以Redis注册成服务
```cmd
# chkconfig --add redis

启动Redis服务
service redis start
```
