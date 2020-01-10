package com.abomb4.rabbitdlx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author yangrl14628 2020-01-02
 */
@Slf4j
public class GoodConsumer {

    @RabbitListener(queues = "#{demoQueue.name}")
    public void receive1(String in) throws InterruptedException {
        receive(in);
    }

    private void receive(String in) throws InterruptedException {
        log.info("收到消息 [{}]，准备一秒后正常消化", in);
        Thread.sleep(1000);
        log.info("消化 [{}] 完成", in);
    }
}
