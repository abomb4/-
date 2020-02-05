package com.abomb4.rabbitdlx;

import com.abomb4.rabbitdlx.common.constants.MqHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author abomb4 2020-01-02
 */
@Slf4j
public class DlxConsumer {

    private final List<DeadLetterEntity> list = Collections.synchronizedList(new ArrayList<>(1000));

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitListener(queues = "#{dlxQueue.name}")
    public void receive1(Message msg) throws InterruptedException {
        receive(msg);
    }

    private void receive(Message msg) throws InterruptedException {
        final String exchange = msg.getMessageProperties().getHeader(MqHeaders.X_FIRST_DEATH_EXCHANGE);
        final String queue = msg.getMessageProperties().getHeader(MqHeaders.X_FIRST_DEATH_QUEUE);
        final String routingKey = msg.getMessageProperties().getReceivedRoutingKey();
        final String body = new String(msg.getBody(), StandardCharsets.UTF_8);
        final DeadLetterEntity deadLetterEntity = new DeadLetterEntity(exchange, queue, routingKey, body);

        log.info("收到已死消息 [{}]，准备一秒后存起来", deadLetterEntity);
        Thread.sleep(1000);
        list.add(deadLetterEntity);
        log.info("已死消息 [{}] 已存储，当前已有 [{}] 个死掉消息", deadLetterEntity, list.size());
    }

    @Scheduled(fixedRate = 3000L)
    public void republishFirst() {
        if (list.size() > 0) {
            final DeadLetterEntity d = list.get(0);
            log.info("准备重发死掉的消息 [{}]", d);
            amqpTemplate.convertAndSend(d.getExchangeName(), d.getRoutingKey(), d.getBody());
            list.remove(0);
            log.info("重发死掉的消息完成，当前剩余 [{}] 个死掉消息", list.size());
        } else {
            log.info("没有死掉的消息，不重发");
        }
    }
}
