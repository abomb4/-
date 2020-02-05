package com.abomb4.rabbitdlx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author abomb4 2020-01-02
 */
@Slf4j
public class BadConsumer {

    @RabbitListener(queues = "#{demoQueue.name}", errorHandler = "rabbitRetryWithRedisErrorHandler")
    public void receive1(String in) throws InterruptedException {
        receive(in);
    }

    private void receive(String in) throws InterruptedException {
        log.info("收到消息 [{}]，准备一秒后爆异常", in);
        Thread.sleep(1000);
        throw new RuntimeException("炸了");
    }
}
