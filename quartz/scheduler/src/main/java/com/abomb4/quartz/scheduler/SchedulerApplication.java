package com.abomb4.quartz.scheduler;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.quartz.*;
import org.springframework.boot.context.properties.*;
import org.springframework.boot.jdbc.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.scheduling.annotation.*;

import javax.sql.*;

/**
 * 启动
 *
 * @author abomb4 2020-01-02
 */
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "quartz-data-source")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
