## 一、服务器配置：
1. 配置网络
```cmd
# vi /etc/sysconfig/network-scripts/ifcfg-eh0
```
2. 设置主机名
```cmd
# vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=edu-mysql-01
```
3. 设置IP与主机名的映射
```cmd
# vi /etc/hosts
127.0.0.1 edu-mysql-01
192.168.1.205 edu-mysql-01
```
4. 两台数据库服务器的selinux都要disable
```cmd
# vi /etc/selinux/config
SELINUX=disabled
```
5. 重启操作系统
```cmd
# reboot
```
## 二、源码安装MYsql5.6.26
1. 使用下面的命令检查是否安装有MySQL Server
```cmd
# rpm -qa | grep mysql

如果是CentOS7以上， 情使用以下命令查看：
# rpm -qa| grep mariadb
```
2. 改防火墙设置， 打开3306端口
```cmd
# vi /etc/sysconfig/iptables
增加如下行：

## MySQL
-A INPUT -p tcp -m state --state NEW -m tcp --dport 3306 -j ACCEPT

重启防火墙
# service iptables restart

```
3. 新增mysql用户组
```cmd
groupadd mysql
```
4. 新增mysql用户， 并添加到mysql用户组
```cmd
useradd -r -g mysql mysql
```
5. 新建MySQL执行文件目录（后面会把编译好的mysql程序安装到这个目录）
```cmd
# mkdir -p /usr/local/mysql
(-p 参数的作用是： 如果最终目录的父目录不存在也会一并创建)
```
6. 新建MYSQL数据库数据文件目录
```cmd
# mkdir -p /home/mysql/data
# mkdir -p /home/mysql/logs
# mkdir -p /home/mysql/temp
```
7. 增加PATH环境变量搜索路径：
```cmd
# vi /etc/profile
## 在 profile 文件末尾增加两行
# mysql env param
PATH=/usr/local/mysql/bin:/usr/local/mysql/lib:$PATH
export PATH

使 PATH搜索路径立即生效：
# source /etc/profile
```
8. 安装编译MySQL需要的依赖包
```cmd
# yum install make cmake gcc gcc-c++ bison bison-devel ncurses ncurses-devel autoconf automake
```
9. 进入/usr/local/src/目录，上传mysql-5.6.26.tar.gz源代码到/usr/local/src目录：
```cmd
# cd /usr/local/src
```
10. 开始编译安装mysql-5.6.26
```cmd
# tar -zxvf mysql-5.6.26.tar.gz
进入解压源码目录
# cd mysql-5.6.26
使用 cmake源码安装mysql（如果你打算安装到不同的路径，注意修改下面语句中 /usr/local/mysql 和 /home/mysql/data路径）

-DCMAKE_INSTALL_PREFIX=/usr/local/mysql ： 设置安装目录
-DMYSQL_DATADIR=/home/mysql/data 设置数据存放目录
-DMYSQL_UNIX_ADDR=/usr/local/msyql/mysql.sock 设置 Unix socket目录
-DMYSQL_USER=mysql 设置运行用户
-DDEFAUT_CHARSET=utf8 设置默认字符集 默认latin1
-DDEFAULT_COLLATION=utf8_general_ci 设置默认校对规则，默认为Latin1_general_ci
-DWITH_INNOBASE_STORAGE_ENGINE=1 添加 InnoDB引擎支持
-DENABLE_DOWNLOADS=1自动下载可选文件，比如自动下载谷歌测试包
-DMYSQL_TCP_PORT=3306 设置服务监听端口，默认是3306
-DSYSCONFDIR=/etc 设置 my.cnf所在目录，默认为安装目录
```
11. cmake结束后开始编译源码，这一步时间会较长
```cmd
# make
```
12. 安装编译好的程序
```cmd
# make install (注意： 如果需要重装mysql， 在/usr/local/src/mysql-5.6.26在执行下make install就可以了，不需要再cmake和make)
```
13. 清除安装临时文件：
```cmd
# make clean
```
14. 修改mysql目录拥有者为mysql用户
```cmd
# chown -Rf mysql:mysql /usr/local/mysql
# chown -Rf mysql:mysql /home/mysql
```
15. 进入mysql执行程序的安装路径：
```cmd
# cd /usr/local/mysql
```
16. 执行初始化配置脚本，创建系统自带的数据库和表
```cmd
# scripts/mysql_install_db --user=mysql --basedir=/usr/local/mysql --datadir=/home/mysql/data
```
17. 初始化脚本在/usr/local/mysql下生成了配置文件my.cnf,需要更改配置文件的所有者：
```cmd
# ls -lah
# chown -Rf mysql:mysql /usr/local/mysql/my.cnf
```
18. 注意：
在启动MYSQL服务时，会按照一定次序搜索my.cnf，先在/etc目录下找，找不到则会搜索mysql程序目录下是否有my.cnf
19. 编译/etc/my.cnf
20. 复制服务启动脚本
```cmd
# cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysql
```
21. 启动MYSQL服务：
```cmd
# service mysql start

```
22. 设置MySQL开机自动启动服务
```cmd
# chkconfig mysql on

设置MySQL数据库 root用户的本地登录密码(初始化用户没有密码）
# mysqladmin -u root password 'roncoo'
```
23. 登录并修改MYSQL用户 root的密码：
```cmd
# mysql -uroot -p
mysql> show databases;
mysql> use mysql;
修改root用户密码：
mysql> update user set Password=password('roncoo.com') where User='root';
mysql> flush privileges;

允许root 远程登录， 设置远程登录密码： jack
mysql> user mysql;
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'jack' WITH GRANT OPTION;
mysql> flush privileges;
mysql> exit;
```
24. 运行安全设置脚本
```cmd
# /usr/local/mysql/bin/mysql_secure_installation
```
25. 重启服务器，检测mysql是否开机自动启动
```cmd
# reboot
```
