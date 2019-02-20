## 1、配置FastDFS存储（192.168.1.135、192.168.1.136、192.168.1.137、192.168.1.138）
### 1.1、复制FastDFS存储样例配置文件，并重命名
```cmd
# cd /etc/fdfs/
# cp storage.conf.sample storage.conf
```
### 1.2、编辑存储样例配置文件（以group1中的storage节点的storage.conf为例）：
```cmd
# vi /etc/fdfs/storage.conf
修改的主要内容
group_name=group1
```
### 1.3、创建一个基础数据目录
```cmd
# mkdir -p /fastdfs/storage
```
### 1.4、防火墙打开存储器端口（默认为2300）
```cmd
# vi /etc/sysconfig/iptables
## FastDFS Storage Port
-A INPUT -m state --state NEW -m tcp -p tcp --dport 23000 -j ACCEPT
重启防火墙
# service iptables restart
```
### 1.5、启动Storage
```cmd
# /etc/init.d/fdfs_storaged start 

查看 23000端口监听情况： netstat -unltp|grep fdfs

所有Storage 节点都启动之后，可以在任意Storage节点上使用如下命令查看集群信息：
# /usr/bin/fdfs_monitor /etc/fdfs/storage.conf
```
### 1.6、关闭Storage：
```cmd
# /etc/init.d/fdfs_storaged stop
```

### 1.7、设置FastDFS存储器开机启动：
```cmd
# vi /etc/rc.d/rc.local
添加
## FastDFS Storage
/etc/init.d/fdfs_storaged start
```
## 2、文件上传测试（192.168.1.131）
### 2.1、修改Tracker服务器中的客户端配置文件：
```cmd
# cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf
# vi /etc/fdfs/client.conf
base_path=/fastdfs/tracker
tracker_server=192.168.1.131:22122
tracker_server=192.168.1.132:22122
```
### 2.2、执行如下文件上传命令
```cmd
# /usr/bin/fdfs_upload_file /etc/fdfs/client.conf /usr/local/src/FastDFS_v5.05.tar.gz
```