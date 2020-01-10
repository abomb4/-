package com.abomb4.rabbitdlx;

import com.abomb4.quartz.common.constants.MqNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * 启动
 *
 * @author abomb4 2020-01-02
 */
@SpringBootApplication
@Slf4j
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
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
	@Profile("bad")
	public BadConsumer badConsumer() {
		log.warn("加载坏蛋");
		return new BadConsumer();
	}

	@Bean
	@Profile("!bad")
	public GoodConsumer goodConsumer() {
		log.info("加载好人");
		return new GoodConsumer();
	}
}
