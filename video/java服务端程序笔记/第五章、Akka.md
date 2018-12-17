## 1、基于服务端开发的常见问题
- 为了提高效率采用多线程以及多线程编程之间的矛盾
    - 多线程编程往往造成效率的极大下降而不是提高
    - 采用阻塞式线程手段容易造成死锁、竞争等更加严重的问题
    - 采用非阻塞式编程需要考虑的问题更多，尽管可以带来效率的提升，但是往往更加容易引入新的问题

- 服务器遭遇故障时的处理手段
    -通过心跳检测判断组件否正常工作
    - 遭遇异常如何处理？
        - checked Exception
        - Unchecked Exception
## 2、Akka简介
- Akka是一个多线程处理的框架，也可以是一个库
    - AkkaSystem负责创建Actor
    - Actor是Akka的核心，每个Actor负责完成一部分任务
    - Actor与Actor之间，通过消息传递内容，完成Actor之间的数据共享
    - Actor传递的消息是不可变的，因此Actor之间没有共享的可变的数据
        - 因为没有共享数据，所以不需要进行额外的线程处理
        - 因为使用消息传递的方式，极大减低Actor之间的耦合度
    - 使用Reactive的模式，提高了系统效率
    - Akka提供了Actor出错后的恢复方式
        - 包括继续执行
        - 停止
        - 重启
        - 向上传递
    - Akka提供了分布式支持
    - Akka的Actor是树形结构
        - 系统会创建用户Actor，用户创建的所有Actor都在用户Actor之下
        - 自根actor起，actor的路径为“/",用户actor的路径为"/user"
        - 以下类似文件路径的方式
    - Actor之间的消息传递是异步的
        - 无法保证不同的actor传递的消息能够按照发送的顺序到达Actor
        - 可以保证同一个Actor传递给另一个一个actor的所有消息的顺序
    - 每一个actor拥有自己的邮箱
        - 消息发送到邮箱
        - Actor 停止时，清空邮箱
        - 可以提供自己的邮箱实现
        - 当actor停止时，所有发送至actor的消息投递到死信邮箱
## Akka编程
- 首先创建一个ActorSystem
    - ActorSystem是一个重量级的对象，其中包含了信箱、线程池，所以每一个应用只需要创建一个即可
- 通过ActorSystem创建的Actor
    - Akka会自动创建两个Actor、一个path是“/system”, 另一个是"/user", 所有用户创建的actor都会在user之下
    - 通过actorOf创建Actor，通过actorFor获取已经创建的actor
- 通过ActorRef.tell发送消息
    - 每一个actor都有context对象，可以从context对象上获取sender，以及自身的ActorRef
## Akka的容错性支持
- 每一个Actor可以watch下级actor，监控其运行状态
- 当Actor运行发生异常时，可以有以下四种策略恢复
    - 继续运行resume
    - 停止运行stop
    - 重启restart
    - 向上传递 escalate

- Actor的恢复策略有两种执行方式
    - One for one strategy 仅仅对发生问题的actor执行
    - One for all strategy, 对所有被监控的actor执行