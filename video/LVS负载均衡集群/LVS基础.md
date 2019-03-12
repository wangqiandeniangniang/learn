
## 1、负载均衡算法
### 1.1、静态负载
- 轮询（round robin)
- 加权轮询（Weighted round robin)
- 目标地址散列 TH
- 源地址散列
## 1.2、动态负载
- 最少连接数  活动连接数x256 + 非活动连接
- 加权最少链接（Weighted Least connection） 默认
- 最短的期望的延迟 （Shortest Expected Delay Scheduling SED） 只考虑活动链接数 （原来链接+1）/权重
- 最少队列调度（Never Queue Scheduling NQ N） 无需队列，如果connect server的链接数=0就直接分配过去，不需要在进行sed运算 活动链接数
- 基于局部性的最少链接（Locality-Based Least Connections)(LBLC)  TH优化 （带缓存）
- 带复制的基于局部性最少链接（Locality-Based Least Connections with Replication)(LBLCR)