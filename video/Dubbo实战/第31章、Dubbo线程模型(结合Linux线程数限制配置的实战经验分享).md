## 1、线程模型
![dubbo线程模型.png](img/dubbo线程模型.png)
- Dispatcher
    - all 所有消息都派发到线程池，包括请求，响应，连接实践，心跳等
    - direct所有消息都不派发到线程池，全部在IO线程上直接执行。
    - message: 只有请求响应消息派发到线程池，其他连接断开事件，心跳等消息，直接在IO线程上执行
    - execution: 只有请求消息派发到线程池，不含响应和其他连接断开实践、心跳等小，直接在IO线程上执行。
    - connection 在IO线程上，将连接断开事件放入队列，有序逐个执行，其他消息派发到线程池
- ThreadPool
    - fixed 固定大小线程池，启动建立线程，不要关闭，一直持有（缺省）
    - cached 缓存线程池，空闲一分钟自动删除，需要时重建。
    - limited 可伸缩线程池，但池中线程数只会增长不会收缩，（为避免收缩时突然来了大流量引起的性能问题）
### 1.1、事件处理线程说明
- 如果事件处理的逻辑能迅速完成，并且不会发起新的IO请求，比如只是在内存中记个标识，则直接在IO线程上处理更快，因为减少了线程池的调度
- 但如果事件处理逻辑较慢， 或者需要发起新的IO请求，比如需要查询数据库，则必须派发到线程池，否IO线程阻塞， 将导致不能接收其他请求
- 如果用IO线程处理事件，又在事件处理过程中发起新的请求，比如在连接事件中发起登录请求，会报“可能引发死锁”异常、但不会真的死锁
### 1.2、配置实例
配置标签：
- <dubbo:provider/>
- <dubbo:protocol/>
```xml
<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100"/>
```
## 2、实战经验风险（属于性能调优）
Linux 用户线程数限制导致的java.lang.OutOfMemoryError:unable to create new native thread 异常
```cmd
# vi /etc/security/limits.d/90-nproc.conf

```
### 2.1、调整时要注意：
1. 尽量不要使用root用户来部署应用程序，避免资源耗尽后无法登陆操作系统
2. 普通用户的线程数限制值要看可用物理内存容量来配置

### 2.2、计算方式
default_nproc=total_memory/128K;

```cmd
$ cat /proc/memeinfo | grep MemTotal
$ echo "5993104 /128" |bc
$ ulimit -u 
```

- ulimit -a # 显示目前资源限制的设定
- ulimit -u # 用户最多可开启的程序数目

重启，使之生效： # reboot
