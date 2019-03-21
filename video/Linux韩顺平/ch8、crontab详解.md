## 1、任务调度
1. 设置任务
- crontab -e
2. 每隔一定时间去执行 date > /home/mydata1
[配置规则](img/crontab.png)

- crontab -r 终止命令调度
- crontab -l 列出所有调度信息