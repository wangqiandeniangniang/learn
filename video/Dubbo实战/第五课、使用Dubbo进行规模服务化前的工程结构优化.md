## 1、 使用Dubbo是为了实现系统的分布式服务化

## 2、做成分布式服务架构的项目特点：
1. 多个服务
2. 多种类型的工程
3. 工程间需要相互调用
4. 如何实现工程间解耦？（高内聚、低耦合）
5. 工程该怎么样拆分？


## 3、新增几个工程
- edu-common(公共工程)   新增
- edu-common-config(公共配置工程) 新增
- edu-common-core(公共core工程) 新增
- edu-common-web （公共web工程） 新增
- edu- facade-user (用户服务接口)
- edu-service-user(用户服务实现)
- edu-web-boss(服务消费者)