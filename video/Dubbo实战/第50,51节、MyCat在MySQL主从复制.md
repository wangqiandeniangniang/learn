## 1、MyCat 介绍
MyCat的读写分离是基于后端MySQL集群的主从同步来实现的，而MyCat提供语句的分发功能，Mycat1.4开始支持MySQL主从复制状态
绑定的读写分离机制，让读更加安全可靠。

## 2、MyCat的安装
1. 设置MyCat的主机名和IP与主机名的映射
```text
# vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=edu-mycat-01

# vi /etc/hosts
127.0.0.1 edu-mycat-01
192.168.1.203 edu-mycat-01
192.168.1.205 edu-mysql-01
192.168.1.206 edu-mysql-02

```
2. MyCat运行JRE环境，并且需要JDK1.7或以上版本
```text
# vi /etc/profile
## java env
export JAVA_HOME=/usr/local/java/jdk1.7.0_72
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib/rt.jar
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin

# source /etc/profile
# java -version
```
3. 创建 mycat 用户并设置密码
```text
# useradd mycat
# passwd mycat

```
4. 上传安装包 mycat-server-1.4 到Mycat服务中/home/mycat目录，并解压并移动到/usr/local/mycat
```text
$ tar -zxvf mycat-server-1.4.tar.gz
$ su root
# mv /home/mycat/mycat /usr/local/
# cd /usr/local/mycat/
# ll

```
5. 设置MyCat的环境变量
```text
# vi /etc/profile
## mycat env
export MYCAT_HOME=/usr/local/mycat
export PATH=$PATH:$MYCAT_HOME/bin

# source /etc/profile

```
## 3、配置MyCat
1.在配置MyCat前，请确认MySQL的主从复制安装配置已完成并正常运行，MySQL主从数据的同步在MYSQL中配置，MyCat不负责数据同步的问题。
    - MySQL主从复制配置中，如果涉及到函数或存储过程的同步复制， 需要在/etc/my.cnf中的[mysqld]段中增加配置 log_bin_trust_function_creators=true
    或在客户端中设置 set global log_bin_trust_function_creators=1;
    - 如果要做读写分离下的主从切换，那么从节点也有可能变为写节点，因此不能设置从节点只读 read_only=1
    - Linux版本的MySQL，需要设置为MYSQL大小写不敏感，否则可能会发生找不到表的问题，在/etc/my.cnf的[mysqld]段中增加lower_case_table_names=1

2. 配置 MyCat 的 schema.xml schema.xml 是 MyCat 最重要的配置文件之一，用于设置 MyCat 的逻辑库、表、数据节点、 dataHost 等内容，
```text
 [mycat@edu-mycat-01 conf]$ cd /usr/local/mycat/conf/
 [mycat@edu-mycat-01 conf]$ vi schema.xml 
```
```xml
<?xml version="1.0"?> 
<!DOCTYPE mycat:schema SYSTEM "schema.dtd"> 
<mycat:schema xmlns:mycat="http://org.opencloudb/"> 
 <!-- 定义MyCat的逻辑库，逻辑库的概念与MySQL中的 database 概念相同 --> 
 <!-- schema name="rc_schema1" checkSQLschema="false" sqlMaxLimit="100" dataNode="rc_dn1"></schema --> 
<!--schema name="pay_schema1" checkSQLschema="false" sqlMaxLimit="100" dataNode="pay_dn1"></schema--> 
 <schema name="rc_schema2" checkSQLschema="false" sqlMaxLimit="100" dataNode="rc_dn2"></schema> 
 <schema name="pay_schema2" checkSQLschema="false" sqlMaxLimit="100" dataNode="pay_dn2"></schema> 
<!-- 其中checkSQLschema表明是否检查并过滤SQL中包含schema的情况，如逻辑库为 TESTDB，则可能写为select * from 
TESTDB.edu_user，此时会自动过滤TESTDB，SQL变为select * from edu_user，若不会出现上述写法，则可以关闭属性为false --> 
<!--sqlMaxLimit默认返回的最大记录数限制，MyCat1.4版本里面，用户的Limit参数会覆盖掉MyCat的sqlMaxLimit默认设置--> 
 
 <!-- 定义MyCat的数据节点 --> 
 <!-- dataNode name="rc_dn1" dataHost="dtHost1" database="roncoo" / --> 
<!-- dataNode name="pay_dn1" dataHost="dtHost1" database="edu_simple_pay" / --> 
 <dataNode name="rc_dn2" dataHost="dtHost2" database="roncoo" /> 
 <dataNode name="pay_dn2" dataHost="dtHost2" database="edu_simple_pay" /> 
<!-- dataNode 中的 name 数据表示节点名称， dataHost表示数据主机名称， database表示该节点要路由的数据库的名称 --> 
<!-- dataHost配置的是实际的后端数据库集群（当然，也可以是非集群） --> 
<!-- 注意：schema中的每一个dataHost中的host属性值必须唯一，否则会出现主从在所有dataHost中全部切换的现象 --> 
<!-- 定义数据主机dtHost1，只连接到MySQL读写分离集群中的Master节点，不使用MyCat托管MySQL主从切换 --> 
<!-- 
<dataHost name="dtHost1" maxCon="500" minCon="20" balance="0" 
writeType="0" dbType="mysql" dbDriver="native" switchType="1" slaveThreshold="100"> 
<heartbeat>select user()</heartbeat> 
<writeHost host="hostM1" url="192.168.1.205:3306" user="root" password="www.roncoo.com" /> 
</dataHost> 
--> 
<!-- 使用MyCat托管MySQL主从切换 --> 
<!-- 定义数据主机dtHost2，连接到MySQL读写分离集群，并配置了读写分离和主从切换 --> 
<dataHost name="dtHost2" maxCon="500" minCon="20" balance="1" 
writeType="0" dbType="mysql" dbDriver="native" switchType="2" slaveThreshold="100"> 
<!-- 通过show slave status检测主从状态，当主宕机以后，发生切换，从变为主，原来的主变为从，这时候show slave  
status就会发生错误，因为原来的主没有开启slave，不建议直接使用switch操作，而是在DB中做主从对调。 --> 
<heartbeat>show slave status</heartbeat> 
<!-- can have multi write hosts --> 
<writeHost host="hostM2" url="192.168.1.205:3306" user="root" password="www.roncoo.com" /> 
<writeHost host="hostS2" url="192.168.1.206:3306" user="root" password="www.roncoo.com" /> 
</dataHost> 
<!-- 参数balance决定了哪些MySQL服务器参与到读SQL的负载均衡中 --> 
<!-- balance="0"，为不开启读写分离，所有读操作都发送到当前可用的writeHost上--> 
<!-- balance="1"，全部的readHost与stand by writeHost参与select语句的负载均衡--> 
<!-- balance="2"，所有读操作都随机的在writeHost、readHost上分发--> 
<!-- MyCat1.4版本中，若想支持MySQL一主一从的标准配置，并且在主节点宕机的情况下，从节点还能读取数据，则需要在MyCat里
配置为两个writeHost并设置balance="1" --> 
<!-- writeType="0"，所有写操作都发送到可用的writeHost上 --> 
<!-- writeType="1"，仅仅对于galera for mysql集群这种多主多节点都能写入的集群起效，此时Mycat会随机选择一个
writeHost并写入数据，对于非galera for mysql集群，请不要配置writeType=1，会导致数据库不一致的严重问题 --> 
</mycat:schema> 

```
3. 配置server.xml
server.xml 主要用于设置系统变量，管理用户，设置用户权限等。
```xml
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE mycat:server SYSTEM "server.dtd"> 
<mycat:server xmlns:mycat="http://org.opencloudb/"> 
 <system> 
 <property name="defaultSqlParser">druidparser</property> 
<property name="charset">utf8mb4</property> 
     <!-- <property name="useCompression">1</property>-->  
     <!--1为开启mysql压缩协议--> 
 <!-- <property name="processorBufferChunk">40960</property> --> 
 <!--  
 <property name="processors">1</property>  
 <property name="processorExecutor">32</property>  
  --> 
  <!--默认是65535 64K 用于sql解析时最大文本长度 --> 
  <!--<property name="maxStringLiteralLength">65535</property>--> 
  <!--<property name="sequnceHandlerType">0</property>--> 
  <!--<property name="backSocketNoDelay">1</property>--> 
  <!--<property name="frontSocketNoDelay">1</property>--> 
  <!--<property name="processorExecutor">16</property>--> 
  <!-- <property name="mutiNodeLimitType">1</property> 0：开启小数量级（默认） ；1：开启亿级数据排序 
       <property name="mutiNodePatchSize">100</property> 亿级数量排序批量 
   <property name="processors">32</property> <property name="processorExecutor">32</property>  
   <property name="serverPort">8066</property> <property name="managerPort">9066</property>  
   <property name="idleTimeout">300000</property> <property name="bindIp">0.0.0.0</property>  
   <property name="frontWriteQueueSize">4096</property>  
<property name="processors">32</property>  
  --> 
 </system> 
 <!-- 用户1，对应的MyCat逻辑库连接到的数据节点对应的主机为MySQL主从复制配置中的Master节点，没实现读写分离，读写都在该
Master节点中进行  --> 
 <!-- 
 <user name="user1"> 
  <property name="password">roncoo.1</property> 
  <property name="schemas">rc_schema1,pay_schema1</property> 
 </user> 
 --> 
 <!-- 用户2，对应的MyCat逻辑库连接到的数据节点对应的主机为主从复制集群，并通过MyCat实现了读写分离 --> 
 <user name="user2"> 
  <property name="password">roncoo.2</property> 
  <property name="schemas">rc_schema2,pay_schema2</property> 
 </user> 
 <!-- 用户3，只读权限--> 
 <user name="user3"> 
  <property name="password">roncoo.3</property> 
  <property name="schemas">rc_schema2,pay_schema2</property> 
  <property name="readOnly">true</property> 
   </user> 
  </mycat:server> 

```
4. 防火墙中打开8066和9066端口
MyCat的默认数据端口为8066，mycat通过这端口接收数据库客户端的访问请求。管理端口为9066，用来接收mycat监控命令、查询mycat运行状态，重新加载
配置文件等
```text
[root@edu-mycat-01 mycat]# vi /etc/sysconfig/iptables 
增加： 
## MyCat 
 -A INPUT -m state --state NEW -m tcp -p tcp --dport 8066 -j ACCEPT 
 -A INPUT -m state --state NEW -m tcp -p tcp --dport 9066 -j ACCEPT 
 重启防火墙：
 [root@edu-mycat-01 mycat]# service iptables restart 
```
5. 修改log日志级别为debug，以便通过日志确认基于MyCat的MySQL数据库集群读写分离的数据操作状态
```text
$ vi /usr/local/mycat/conf/log4j.xml
```
6. 启动MyCat
```text
$ cd /usr/local/mycat/bin/
$ ./mycat console  # 控制台启动
$ ./mycat start # 后台运行

```
7. MyCat连接测试
```text
$ mysql -uuser2 -proncoo.2 -h192.168.1.203 -P8066 
```
8. 读写分离测试
- MyCat 打印出来日志信息显示读操作操作请求都市路由到Slave节点
- MyCat 打印出来日志信息显示写操作请求都是路由到master节点

