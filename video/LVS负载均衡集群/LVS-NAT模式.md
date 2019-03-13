
## 1、 安装 ipvsadm

```text
# yum -y install ipvsadm
# yum -y install httpd

# service httpd restart  //暗示是那台真实机器返回

```

## 2、ipvsadm 操作
```text
# ipvsadm -A -t 10.10.10.1:80 -s rr  //-A添加， -t tcp 10.10.10.1:80 调度器vip， -s 调度算法 rr(round robin轮询)

# ipvsadm -a -t 10.10.10.1:80 -r 192.168.10.11 -m  -w 1 // -a 添加真实服务，  -r真实  -m maskgrep(nat模式)， -w 权重

# ipvsadm -L -n  //查询当前
# service ipvsadm save // 保存配置（跟iptables类似）
# ipvsadm -E -t 10.10.10.1:80 -s wrr //修改调度算法为 权重轮询(weighted round robin)

-- 以下是真实服务器设置
# iptables -L (真实服务器上清空防火墙)
# cd /etc/sysconfig/network-scripts/  
# vim ifcfg-eth0  //修改网关 GATAWAY 为nat服务器的IP
# service network restart
```