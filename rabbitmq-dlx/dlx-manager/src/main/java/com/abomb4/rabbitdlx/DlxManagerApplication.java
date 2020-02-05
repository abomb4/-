package com.abomb4.rabbitdlx;

import com.abomb4.rabbitdlx.common.constants.MqNames;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动
 *
 * @author abomb4 2020-01-02
 */
@SpringBootApplication
@EnableScheduling
public class DlxManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlxManagerApplication.class, args);
	}

	/**
	 * dlx exchange
	 *
	 * @return exchange
	 */
	@Bean("dlxExchange")
	public TopicExchange dlxExchange() {
		return new TopicExchange(MqNames.DLX_EXCHANGE_NAME);
	}

	/**
	 * dlx 接收
	 *
	 * @return 队列
	 */
	@Bean("dlxQueue")
	public Queue dlxQueue() {
		return new Queue(MqNames.DLX_QUEUE_NAME);
	}

	@Bean
	public Binding binding1b(TopicExchange dlxExchange, Queue dlxQueue) {
		return BindingBuilder.bind(dlxQueue)
				.to(dlxExchange)
				.with("#");
	}
	@Bean
	public DlxConsumer dlxConsumer() {
		return new DlxConsumer();
	}
}
