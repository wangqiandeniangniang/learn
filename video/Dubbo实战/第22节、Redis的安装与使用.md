## 1、编译和安装需要的包
````
# yum install gcc tcl
````
## 2、下载Redis版本
````
# cd /usr/local/src
# wget https://github.com/antirez/redis/archive/3.0.0-rc5.tar.gz

创建安装目录
# mkdir /usr/local/redis

解压
# tar -zxvf 3.0.0-rc5.tar.gz
# mv redis-3.0.0-rc5 redis3.0
# cd redis3.0

安装（使用PREFIX指定安装目录）
# make PREFIX=/usr/local/redis install
安装完成后，可以看到/usr/local/redis 目录下有一个bin目录，bin目录里就是redis的命令脚本
redis-benchmark, redis-check-aof ,redis-check-dump redis-cli redis-server

````
## 3、配置Redis
````
将Redis配置成服务：
将上面的操作步骤，Redis的启动脚本为： /usr/local/src/redis3.0/utils/redis_init_script
将启动脚本复制到/etc/rc.d/init.d/目录下， 并命名为redis：
# cp /usr/local/src/redis3.0/utils/redis_init_script /etc/rc.d/init.d/redis

编辑/etc/rc.d/init.d/redis. 修改相应配置，使之能注册成服务。
# vi /etc/rc.d/init.d/redis

1.脚本的第一行后面添加一行内容如下：
#chkconfig: 2345 80 90   (如果不添加这行内容，在注册服务时会提示：service redis does not support chkconfig)
2. REDISPORT 端口保持6379不变，（注意，端口名将于下面的配置文件名有关）
3. EXEC=/usr/local/bin/redis-server 改成 EXEC=/usr/local/redis/bin/redis-server
4. CLIEXEC=/usr/local/bin/redis-cli 改成 CLIEXEC=/usr/local/redis/bin/redis-cli
5.配置文件设置
创建 redis配置文件目录
# mkdir /usr/local/redis/conf

复制Redis配置文件/usr/local/src/redis3.0/redis.conf到/usr/local/redis/conf 目录并按端口重命名为6379.conf

# cp /usr/local/src/redis3.0/redis.conf /usr/local/redis/conf/6379.conf
做了以上准备后，在对CONf属性作如下调整
CONF="/etc/redis/${REDISPORT}.conf" 改为 CONF="/usr/local/redis/conf/${REDISPORT}.conf"
6. 更改redis开启的命令，以后台运行的方式执行：
$EXEC $CONF &   # "&"作用是将服务转到后面运行

以上配置操作完成后， 便可以将Redis注册成为服务
# chkconfig --add redis

防火墙中打开对应的端口
# vi /etc/sysconfig/iptables

添加
-A INPUT -m state --state NEW -m tcp -p tcp --dport 6379 -j ACCEPT

重启防火墙
# service iptables restart

修改redis配置文件
# vi /usr/local/redis/conf/6379.conf

修改如下配置
daemonize no 改为 > daemonize yes
pidfile /var/run/redis.pid 改为  pidfile /var/run/redis_6379.pid

启动Redis服务
# service redis start

# 将Redis 添加到环境变量中：
# vi /etc/profile
最后添加一下内容
## Redis env
export PATH=$PATH:/usr/local/redis/bin

使得配置生效：
# source /etc/profile

现在就可以直接使用redis-cli等redis命令了：

# service redis stop

默认情况下，Redis开启安全认证、可以通过/usr/local/redis/conf/6379.conf的requirepass指定一个验证密码
````