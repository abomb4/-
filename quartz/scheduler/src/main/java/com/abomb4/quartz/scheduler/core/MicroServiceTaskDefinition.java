package com.abomb4.quartz.scheduler.core;

import lombok.*;

/**
 * 固定微服务任务的定义，就是存在数据库里面的数据结构。
 *
 * @author abomb4 2020-01-10
 */
@Data
public class MicroServiceTaskDefinition {

    /** 任务名 */
    private String taskName;

    /** 任务描述 */
    private String taskDescription;

    /** cron 表达式 */
    private String cronExpression;

    /** 微服务名 */
    private String serviceName;

    /** 只要后半部分，不包括前面应用名 */
    private String url;

    /** Json 串，要支持动态参数的替换 */
    private String jsonContent;
}
