## 1、 使用Dubbo是为了实现系统的分布式服务化

## 2、做成分布式服务架构的项目特点：
1. 多个服务
2. 多种类型的工程
3. 工程间需要相互调用
4. 如何实现工程间解耦？（高内聚、低耦合）
5. 工程该怎么样拆分？


## 3、新增几个工程
- edu-common(公共工程)   新增
    - constants : 常量
    - entity : 实体
    - enums : 枚举类
    - exceptions :异常
    - page ： 分页
    - utils: 工具类
- edu-common-config(公共配置工程) 新增
    - JDBC连接配置
    - 公共服务属性，例如dubbo的ip地址+port
- edu-common-core(公共core工程) 新增
    - 核心操作，这里引用了edu-common的包作为依赖， 对数据库公共的抽象的操作
- edu-common-web （公共web工程） 新增
    - 引用edu-common的包
- edu- facade-user (用户服务接口)
    - 引用edu-common的包， 删除跟user无关的实体类和枚举
- edu-service-user(用户服务实现)
    - 引入 edu-common
    - 引入 edu-common-config
    - 引入 edu-facade-user
    - 引入 edu-common-core
- edu-web-boss(服务消费者)