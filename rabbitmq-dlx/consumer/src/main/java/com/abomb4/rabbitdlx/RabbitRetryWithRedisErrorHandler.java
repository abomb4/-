package com.abomb4.rabbitdlx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.util.ErrorHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 利用 Redis 存储消息失败次数，没到次数则Requeue，达到次数则 RejectAndNoRequeue；
 * 所有异常都视为处理失败
 *
 * @author yangrl14628 2020-02-05
 */
@Slf4j
public class RabbitRetryWithRedisErrorHandler implements RabbitListenerErrorHandler {

    private static final int MAX = 3;
    private final Map<String, AtomicInteger> map = new ConcurrentHashMap<>();

    @Override
    public Object handleError(final Message amqpMessage, final org.springframework.messaging.Message<?> message,
            final ListenerExecutionFailedException exception) throws Exception {

        if (retryCountExceed(amqpMessage)) {
            throw new AmqpRejectAndDontRequeueException("重试次数达到限制，投入DLX。", exception);
        } else {
            throw exception;
        }
    }

    private boolean retryCountExceed(final Message amqpMessage) {
        // check retry count
        final String messageId = amqpMessage.getMessageProperties().getMessageId();
        final String msg = new String(amqpMessage.getBody());
        if (messageId == null) {
            log.warn("消息 [{}] 没有id，直接算作过期", msg);
            return true;
        }
        final AtomicInteger count = map.compute(messageId, (key, value) -> {
            if (value == null) {
                return new AtomicInteger(0);
            } else {
                return value;
            }
        });

        final int i = count.incrementAndGet();

        log.debug("消息 [{}] 的重试次数为 [{}] ，最大重试次数为 [{}]", msg, i, MAX);
        final boolean b = i > MAX;

        if (b) {
            map.remove(messageId);
        }
        return b;
    }
}
