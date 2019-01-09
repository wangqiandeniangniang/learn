## 1、安装JDK并配置环境变量
JAVA_HOME=/usr/local/java/jdk1.7.0_80

## 2、下载Linux版的ActiveMQ(apache-activemq-5.11.1-bin.tar.gz)
$ wget http://apache.fayea.com/activemq/5.11.1/apache-activemq-5.11.1-bin.tar.gz

## 3、解压安装
````
#  tar -zxvf apache-activemq-5.11.1-bin.tar.gz
# mv apache-activemq-5.11.1 activemq-01

如果脚本activemq没有可以执行权限，此时则需要授权（此步骤可选）
# cd /home/wusc/activemq-01/bin/
# chmod 755 ./activemq
````

## 4、防火墙中打开对应的端口
ActiveMQ需要用到两个端口
- 一个是消息通讯的端口（默认为61616）
- 一个是管理控制台端口(默认为8161) 可以在conf/jetty.xml中修改配置
````
# vi /etc/sysconfig/iptables
添加如下

-A INPUT -m state --state NEW -m tcp -p tcp --dport 61616 -j ACCEPT
-A INPUT -m state --state NEW -m tcp -p tcp --dport 8191 -j ACCEPT

重启防火墙
# service iptables restart
````

## 5、启动
````
# cd /home/wusc/activemq-01/bin
# ./activemq start
````

## 6、 打开管理界面： http://192.168.230.133:8161
默认登录密码时admin/admin

## 7、安全配置（消息安全）
> ActiveMQ 如果不加人安全机制的话，任何人只要知道消息服务的具体地址（包括IP，端口，消息地址【队列或主题地址】）都可以肆无忌惮的发送、接收消息，
详情可以参考http://activemq.apache.org/security.html

ActiveMQ de 消息配置安全配置策略有多种，我们以简单授权配置为例
在conf/activemq.xml文件中在broker 标签最后加入以下内容即可：
# vi /home/wusc/activemq-01/conf/activemq.xml

````
<plugins>
    <simpleAuthenticationPlugin>
        <users>
            <authenticationUser username="wusc" password="wusc" groups="users,amdins"/>
        <users>
    </simpleAuthenticationPlugin>
</plugins>
````
定义一个wusc用户，密码为wusc、角色为users/admins

设置admin用户名和密码
````
# vi /home/wusc/activemq-01/conf/jetty.xml

<bean id="securityConstraint" class="org.eclipse.jetty.util.security.Constraint">
    <property name="name" value="BASIC"/>
    <property name="roles" value="admin"/>
    <property name="authenticate" value="true"/>
</bean>
确保authenticate的值为true （默认）

控制台的登录用户名密码保存在conf/jetty-realm.properties 文件中，内容如下
$ vi /home/wusc/activemq-01/conf/jetty-realm.properties

admin: wusc.123, admin

注意： 用户名和密码的格式是
用户名： 密码 , 角色

重启
$ /home/wusc/activemq-01/bin/activemq restart

设置开机启动:
# vi  /etc/rc.local

加入一下内容
##ActiveMQ
su - wusc -c '/home/wusc/activemq-01/bin/activemq start'

````