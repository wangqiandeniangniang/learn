## 1、FastDFS简介
    - FastDFS 是一个轻量级的开源分布式文件系统
    - FastDFS 主要解决了大容量的文件存储和高并发访问的问题，文件存取时实现了负载均衡
    - FastDFS实现了软件方式的RAID、可以使用廉价的IDE硬盘进行存储
    - 支持存储服务器在线扩容
    - 支持相同内容的文件只保存一份、节约磁盘空间
    - FastDFS只能通过ClientAPI访问， 不支持POSIX访问方式
    - FastDFS特别适合大中型网站使用，来用存储资源文件（如： 图片、文档、音频、视频等等）

## 2、系统架构
类似有一个目录 和协调
内容
![FastDFS架构图](img/ch23/FastDFS架构图.jpg)

### 2.1、上传文件流程图
![上传文件流程图](img/ch23/上传文件流程图.jpg)
1. client询问tracker上传到的storage ， 不需要附加参数；
2. tracker返回一台可用的storage
3. cleint直接和storage通讯完成文件上传

### 2.2、下载文件流程图
![下载文件流程图](img/ch23/下载文件流程图.jpg)
1. client询问tracker下载文件的storage，参数为文件标识（组名和文件名）
2. tracker 返回一台可用的storage
3. client 直接和storage通讯完成文件下载
## 3、相关术语
- Tracker Server：跟踪服务器，主要做调度工作，在访问上起到负载均衡的作用。记录storage server的状态，是连接Client和Storage server的纽带
- Storage Server： 存储服务器，文件和meta data都保存到存储服务器上
- group :组，可以称为卷。 同组内服务器上的文件是完全相同的
- 文件标识：包括两部分：组名和文件名（包含路径）
- meta data: 文件相关属性、键值对（Key Value Pair）方式， 如： width=1024,height=768

## 4、同步机制
- 同一组内的storage server之间是对等的，文件上传、删除等操作可以在任意一台storage server上进行
- 文件同步只在同组内的storage server之间进行，采用push方式，即源服务器同步给目标服务器。
- 源头服务器才需要同步、备份数据不需要再次同步，否则就构成环路了
- 上述第二条规则有关例外，就是新增一台storage server时，由已有的一台storage server将已有的所有数据（包括源头数据和备份数据）同步给该新服务器。

## 5、通信协议
- 协议包由两个部分组成： header 和body
- header共有10字节、格式如下
    - 8 bytes body length
    - 1 byte command
    - 1 byte status
- body数据格式由取决于具体的命令，body可以为空

## 6、目录结构
### 6.1、tracker server
- ${base_path}
    - data
        - storage_groups.dat ： 存储分组信息
        - storage_servers.dat : 存储服务器列表
    - logs
        - trackerd.log: tracker server日志文件

### 6.2、storage server
- ${base_path}
    - data
        - data_init_flag: 当前storage server初始化信息
        - storage_stat.dat: 当前storage server统计信息
        - sync ：存储数据同步相关文件
            - binlog.index : 当前的binlog文件索引号
            - binlog.###: 存放更新操作记录（日志）
            - ${ip_addr}_${port}.mark: 存放同步的完成情况

        - 一级目录：256个存放数据文件的目录，如00， 1F
            二级目录：256个存放数据文件的目录
    - logs
        - storaged.log: storage server 日志文件

## 7、安装和运行
- step 1. download FastDFS source package and unpack it,
````
# if you use HTTP to download file, please download libevent 1.4.x and install it
tar xzf FastDFS_v1.x.tar.gz

#for example:
tar xzf FastDFS_v1.20.tar.gz
````
- step 2. enter the FastDFS dir
````
cd FastDFS
````
- step 3. if HTTP supported, modify make.sh, uncomment the line:
````
# WITH_HTTPD=1, then execude:
./make.sh

````

- step 4 make install
````
./make.sh install
````
- step 5. edit/modify the config file of tracker and storage

- step 6. run server programs
````
# start the tracker server:
/usr/local/bin/fdfs_trackerd <tracker_conf_filename>

# start the storage server
/usr/local/bin/fdfs_storaged <storage_conf_filename>
````

## 8、和其他系统对比
| 指标     | FastDFS| NFS | 集中存储设备如NetApp- NAS|
| ------------- | ------------- | ------------- | ------------- |
|线性扩容性 | 高 | 差| 差|
| 文件高并发访问性能  | 高 | 差 | 一般|
| 文件访问方式  | 专有API | POSIX| 支持POSIX|
| 硬件成本  | 较低 | 中等 | 高|
| 相同内容文件只保存一份  | 支持 | 不支持 | 不支持|

- FastDFS和mogileFS对比
![fastDFS_mogileFS](img/ch23/fastDFS_mogileFS.jpg)

## 参考网站
- [FastDFS中文](http://www.csource.org/,"")
- [FastDFS英文](http://code.google.com/p/fastdfs/,"")