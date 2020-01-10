package com.abomb4.rabbitdlx;

import com.abomb4.quartz.common.constants.MqNames;
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
public class SupplierApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplierApplication.class, args);
	}

	/**
	 * 用于发布消息的 exchange
	 *
	 * @return exchange
	 */
	@Bean("demoExchange")
	public TopicExchange demoExchange() {
		return new TopicExchange(MqNames.DEMO_EXCHANGE_NAME);
	}

	/**
	 * 自动分配业务角色的队列
	 *
	 * @return 队列
	 */
	@Bean("demoQueue")
	public Queue demoQueue() {
		return new Queue(MqNames.DEMO_QUEUE_NAME);
	}

	@Bean
	public Binding binding1b(TopicExchange topicExchange, Queue memberQueue) {
		return BindingBuilder.bind(memberQueue)
				.to(topicExchange)
				.with("#");
	}

	@Bean
	public Supplier supplier() {
		return new Supplier();
	}
}
