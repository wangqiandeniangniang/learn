## 1、追踪路由
- tracert 目标ip/域名
## 2、测试两个IP是否通畅
- ping 目标IP
## 3、 在window查看IP情况的命令是
- ipconfig
## 4、在linux/unix下查看IP情况的命令是
- ifconfig
## 5、如何在vm上配置固定ip地址
[配置固定IP](https://jingyan.baidu.com/article/ff42efa9d58ae4c19e2202a1.html)
第一种方式
- setup   进入界面设置
第二种方式 临时修改
- ifconfig eth0 x.x.x.x 对网卡进行设置
- ifconfig eth0 network x.x.x.x 对子网掩码设置

第三种方式
- 修改/etc/sysconfig/network-scripts/ifcfg-eth0 这个文件里各个属性可以修改，包括IP，子网掩码，广播地址，默认网关。DNS1
DNS和默认网关设置同一IP地址
- 这时候网卡的配置没有生效，运行/etc/rc.d/init.d/network restart 命令我们刚才做的设置才生效