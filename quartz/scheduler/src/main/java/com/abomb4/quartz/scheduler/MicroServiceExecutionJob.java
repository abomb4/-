package com.abomb4.quartz.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.*;

/**
 * 微服务调用任务
 *
 * @author abomb4 2020-01-10
 */
public class MicroServiceExecutionJob implements Job {

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        if (beanFactory == null) {
            throw new IllegalStateException("Bean factory is not set");
        }
    }
}
