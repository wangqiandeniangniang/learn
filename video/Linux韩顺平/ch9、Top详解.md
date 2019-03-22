## 1、top 与 ps 命令很相似
top 动态的， ps是静态的
- 监控特定用户
top：输入此命令，按回车键，查看执行的进程，
u:然后输入 u 回车， 再输入用户名，即可

- 终止指定的进程
top : 输入此命令、按回车键， 查看执行的进程
k： 然后输入k回车，再输入要结束的进程Id号

- 指定系统状态更新的时间
 top -d 10: 指定系统更新的进程的时间为10秒
 [top分析](img/top分析.png)

## 2、设置系统日期
- 1、date命令：显示系统时间
- 2、 利用date命令来更改系统的时间
```text
date MMDDHHMMCCYY.SS     月月日日时时分分年年年年.秒秒
```
- 3、查看日历
```text
cal 3 2002: 查看2002年3月的月历
```
- 4、查看年历
```text
cal 2008 : 查看2008的年历
```
## 3、监控网络状态信息 netstat

- 监控数据包经过历程的命令 traceroute

## 4、MySQL安装
```text
groupadd mysql （创建mysql组）
useradd -g mysql mysql (创建mysql用户，并放入到mysql组)
进入到mysql的文件夹
scripts/mysql_install_db  --user=mysql  (初始化数据库)
chown -R root .  (修改文件的所有者)
chown -R mysql data (修改data文件夹的所有者)
chgrp -R mysql .( 改变用户组)

启动mysql
bin/mysqld_safe --user=mysql & [& 表示以后台的方式启动]
检查一下， ok 正常启动

如果希望在任意一个目录度可以进入mysql则需要在/root/.bash_profile中添加路径。

```
## 5、如何使用命令行来备份和恢复mysql数据库
备份： mysqldump -u root -p密码 数据库名 > data.bak
恢复： mysql -u root -p密码 数据库名  < data.bak