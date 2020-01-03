# 死信队列怎么用
研究一下怎么用。

## 死信机制介绍
按照官网的教程（https://www.rabbitmq.com/dlx.html），RabbitMQ 提供了 DLX 功能。
它规定当某消息满足某些条件时，会将消息发布到一个 `DLX Exchange` ，之后用户可以自行对 `DLX Exchange` 进行监听处理。

“死信”发生条件包括：

- 消息被消费者明确拒绝，响应 `basic.reject` 或响应 `basic.nack` 并且 `requeue` 参数设置为 `false`
- 消息的 `message-ttl` 过期
- 由于队列慢造成的消息丢弃

RabbitMQ 提供的 DLX 功能可以使所有队列产生死信时都发布消息到 `DLX Exchange` ，也可以为每个队列指定 `DLX Exchange` ，
还可以更改消息的 `routingKey`。

## 实验方法
本次实验功能为，所有消息都放到 `DLX Exchange` ，实现死信处理程序，死信处理程序存储死信，并定时重发。

实验利用了 Docker ，可在三大主流 PC 操作系统中执行。

操作步骤：
1. 使用 Docker 启动 RabbitMQ
    ```
    docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    ```
    
2. 找到 `rabbitmqctl` 命令，这里推荐使用 `docker exec` 方法
    - 先执行 `docker ps` ，找到 RabbitMQ 启动容器；
    - 再执行 `docker exec -it ContainerId /bin/bash` 进入。

3. 配置 RabbitMQ：
    - 创建 vhost ：`rabbitmqctl add_vhost scalable-t`
    - 创建用户： `rabbitmqctl add_user scalable-t 12341234`
    - 分配权限： `rabbitmqctl set_permissions -p scalable-t scalable-t ".*" ".*" ".*" `
    - 创建 dlx 规则：`rabbitmqctl set_policy DLX ".*" '{"dead-letter-exchange":"dlx-exchange"}' --apply-to queues -p scalable-t`

4. 启动测试程序进行验证，以下以 `IDEA` 为预设 IDE 进行说明
    - 先启动 `dlx-manager`
    - 再启动 `consumer`
    - 增加另一个 `consumer` 的启动配置，在其 `VM Options` 里面添加 
        `-Dspring.profiles.active=bad -Dserver.port=18781` ，并启动
    - 启动 `supplier`
    
此时 `supplier` 会不停发消息，消息会平均 dispatch 到两个 `consumer` ，一个会报异常，一个正常处理；
会看到报异常的消息会在 `dlx-manager` 中收到并重发。

## 实验要点
1. 要预先定义好死信 `exchange` 和死信 `queue`，及其绑定关系，否则消息会丢失
2. `spring-rabbit` 对于消费异常的处理，默认是 `requeue` ，不会进入死信队列，需要抛出 
    `org.springframework.amqp.AmqpRejectAndDontRequeueException` ，或进行 `properties` 配置（这次没试出来）

## 结论
这次的实验成功利用了 DLX 机制，但仍有不足：
- 业务代码一般不会抛出 `AmqpRejectAndDontRequeueException` ，最好有失败次数的机制。
    - 感觉在 `requeue` 步骤做一些操作，添加一个自定义头，在消费端实现类似 `ICMP` `TTL` 机制的东西
- MQ 配置方法可能并不完美
