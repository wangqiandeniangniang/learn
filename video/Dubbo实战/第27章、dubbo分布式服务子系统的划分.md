## 1、服务化的目标
- 将系统中独立的业务模块抽取出来，按业务的独立性进行垂直划分，抽象出基础服务层
- 基础服务为上游业务的功能实现提供了支撑，基础服务应用本身无状态，可以随着系统的负荷灵活伸缩来提供服务能力
-
## 2、服务子系统的数量把控
1. 过多： 可以划分过细，破坏业务子系统的独立性
    （如： 支付订单、退款订单，用户、账户）
    部署维护工作量大，独立进程占用内存多
2. 过少： 没有很好的解耦、开发委会不好分工、升级维护影响面大

## 3、服务子系统划分的注意事项
- 不要出现A服务中的SQL需要连接查询到B服务中的表等情况， 这样在A服务与B服务进行垂直拆库时就会出错。
- 服务子系统间避免出现环状的依赖调用
- 服务子系统间的依赖关系链不要过长
- 尽量避免分布式事务。

## 4、服务子系统系统的划分是一个不断优化的过程