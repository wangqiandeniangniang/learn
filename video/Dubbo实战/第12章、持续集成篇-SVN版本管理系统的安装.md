## 1、安装Apache

- 建议安装更新操作系统
````shell
# yum update
````
更新完系统后重启
````
# reboot
````
- 安装apache
````
# yum install httpd httpd-devel
# service httpd start
# chkconfig httpd on  开机启动

# vi /etc/httpd/conf/httpd.conf
找到 Servicename 并修改成
ServerName localhost:80  配置apache ip+port

防火墙中打开 80 端口：
# vi /etc/sysconfig/iptables

-A INPUT -m state --state NEW -m tcp -p tcp --dport 80 -j ACCEPT

# service iptables restart
然后 输入 http://服务器ip/
````
## 2、安装SVN服务
````
# yum install mod_dav_svn subversion
必须安装mod_dav_svn模块

安装完svn后重启apache
# service httpd restart

查看测试是否安装Svn模块
# ls /etc/httpd/modules/ | grep svn
mod_authz_svn.so
mod_dav_svn.so
# svn --version  查看svn版本

创建svn库主目录（多库模式、一份配置文件管理多个库）
# mkdir /svn/

# cd /etc/httpd/conf.d
# ls
此时可以看到一个subversion.conf配置文件(是在安装mod_dav_svn模块时生成的)
如果没有看到
# cd /etc/httpd/conf.modules.d   目录
10-subversion.conf

# vi subversion.conf  或 vi 10-subversion.conf

添加以下内容
#Include /svn/httpd.conf
<Location /svn/>
DAV svn
SVNListParentPath on
SVNParentPth /svn
AuthType Basic
AuthName "Subversion repositories"
AuthUserFile /svn/passwd.http
AuthzSVNAccessFile /svnn/authz
Require valid-user
</Location>
RedirectMatch ^(/svn)$ $1/



创建 /svn/passwd.http 和 /svn/authz

# touch /svn/passwd.http
# touch /svn/authz

重启 apache
# service httpd restart

````
## 3、 安装jsvnadmin
svnadmin介绍： （在Google Code 上，需要FQ才能下载[jsvnadmin] (https://jsvnadmin.googlecode.com/files/svnadmin-3.0.5.zip)）

## 4、安装MySQL 用于jsvnadmin使用
````
# rpm -qa | grep mysql ## 查看该系统上是否已经安装mysql数据库， 有的话，可以通过rpm -e命令，或者rpm -e --nodeps来卸载

# yum install mysql-server mysql mysql-devel
# service mysqld start

# chkconfig --list | grep mysqld

# chkconfig mysqld on

为了方便远程管理，防火墙中打开3306 端口
# vi /etc/sysconfig/iptables
-A INPUT -m state --state NEW -m tcp -p tcp --dport 3306 -j ACCEPT

重启防火墙， 使得端口配置生效
# service iptables restart

设置MySQL数据库root用户密码
# mysqladmin -u root password 'root'
登录数据库
# mysql -u root -p

MySQL 授权远程访问(先用root登录mysql)
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;
mysql> FLUSH PRIVILEGES;
````
## 5、 使用Tomcat7 部署svnadmin
````
# cd /root
使用 wget下载最新版的tomcat7 的tar.gz包

# tar -zxvf apache-tomcat-7.0.xx.tar.gz
# mv apache-tomcat-7.0.xx svnadmin-tomcat

修改Tomcat的端口为9000 和容器编码为UTF-8
# vi /root/svnadmin-tomcat/conf/server.xml
<Server port="9005" shutdown="SHUTDOWN">
<Connector port="9000" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" URIEncoding="UTF-8"/>

防火墙中打开9000端口
# vi /etc/sysconfig/iptables
-A INPUT -m state --state NEW -m tcp -p tcp --dport 9000 -j ACCEPT
重启防火墙， 使得端口配置生效
# service iptables restart

# cd /root/svnadmin-tomcat/webapps
# rm -rf *

上传svnadmin.war到/root/svnadmin-tomcat/webapps目录
# cd /root/svnadmin-tomcat/webapps
解压
# unzip svnadmin.war -d svnadmin
备份
# mv svnadmin.war /root/tools/
# cd svnadmin/WEB-INF
# vi jdbc.properties
内容如下
db=MySQL
#MySQL
MySQL.jdbc.driver=com.mysql.jdbc.Driver
MySQL.jdbc.url=jdbc:mysql://127.0.0.1:3306/svnadmin?characterEncoding=utf-8
MySQL.jdbc.username=root
MySQL.jdbc.password=root

创建 svnadmin数据库并导入相应数据（UTF-8编码）
执行 db/mysql5.sql 和 db/lang/en.sql

启动svnadmin-tomcat
# /root/svnadmin-tomcat/bin/startup.sh
浏览器中打开： http://服务ip:9000/svnadmin/
创建项目

配置库目录 权限 , apache用户可以读写项目文件  ，每新增一个工程需要做如下步骤
# cd /svn
# chown -R apache.apache 工程名
# chmod -R 777 工程名

关闭SElinux （Linux的访问控制）
修改/etc/selinux/config文件
# vi /etc/selinux/config
将 SELINUX=enforcing 改为 SELINUX=disabled
重启机器即可
# reboot

安装SVN管理客户端 TortoiseSVN ，方便对SVN库的操作和管理
(1) 用户组
（2）用户
（3）授权
（4）导入项目

````










