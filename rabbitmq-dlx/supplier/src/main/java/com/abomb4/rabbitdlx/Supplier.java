package com.abomb4.rabbitdlx;

import com.abomb4.rabbitdlx.common.constants.MqNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每 5 秒发一个消息
 *
 * @author abomb4 2020-01-02
 */
@Slf4j
public class Supplier {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Scheduled(fixedRate = 5000L)
    public void send() {
        // 发一条消
        final String msg = "[" + ATOMIC_INTEGER.incrementAndGet() + "] The 消息 @@@@";
        log.info("发个消息：[{}]", msg);
        final Boolean sent = amqpTemplate.invoke(operations -> {
            operations.convertAndSend(MqNames.DEMO_EXCHANGE_NAME, MqNames.DEMO_TOPIC, msg);
            return amqpTemplate.waitForConfirms(3000L);
        });
        if (sent) {
            log.info("消息 [{}] 发送成功", msg);
        } else {
            log.error("消息 [{}] 发送失败", msg);
        }
    }
}
