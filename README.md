# 死信队列怎么用
研究一下怎么用。

使用 Docker 快速启动 RabbitMQ 的方法：
```
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
主机可能没有 rabbitmqctl 命令，需要进入容器或安装。安装太麻烦所以使用进入容器的方法。

进入 Docker 容器的方法：
- 先执行 `docker ps` ，找到 RabbitMQ 启动镜像；
- 再执行 `docker exec -it ContainerId /bin/bash` 进入。

创建测试数据：
- 创建 vhost ：`rabbitmqctl add_vhost scalable-t`
- 创建用户： `rabbitmqctl add_user scalable-t 12341234`
- 分配权限： `rabbitmqctl set_permissions -p scalable-t scalable-t ".*" ".*" ".*" `
- 创建 dlx 规则：`rabbitmqctl set_policy DLX ".*" '{"dead-letter-exchange":"dlx-queue"}' --apply-to queues -p scalable-t`



