- 方法1、
[linux安装mysql](https://www.cnblogs.com/bookwed/p/5896619.html)

- 方法2、
# rpm -qa | grep mysql  ### 查看该操作系统是否已经安装了mysql数据库，有的话，可以通过
rpm -e 命令 或 rpm -e --nodeps 命令来卸载

# yum install mysql-server mysql mysql-devel
````
 这里可能会出现 没有可用软件包 mysql-server  参考[没有可用mysql-server](https://blog.csdn.net/aizhiqiang2/article/details/51685308)
wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
 # rpm -ivh mysql-community-release-el7-5.noarch.rpm
 # yum install mysql-server
 ````

# service mysqld start

# chkconfig  --list | grep mysqld

# chkconfig mysqld on (开机启动)

# vi /etc/sysconfig/iptables  开启3306端口
-A INPUT -m state --state NEW -m tcp -p tcp --dport 3306 -j ACCEPT
# service iptables restart  重启防火墙
# mysqladmin  -u root password 'root'  设置root的密码为root
# mysql -u root -p  登录数据库

# mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;  mysql远程授权访问（先用root登录mysql）

# mysql> FLUSH PRIVILEGES; 权限生效

