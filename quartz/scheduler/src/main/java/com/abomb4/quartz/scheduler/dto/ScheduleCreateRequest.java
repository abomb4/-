package com.abomb4.quartz.scheduler.dto;

import com.abomb4.quartz.scheduler.core.EnumJobType;
import lombok.*;

/**
 * 创建计划请求
 *
 * @author abomb4 2020-01-10
 */
@Data
public class ScheduleCreateRequest {
    private EnumJobType jobType;

    private String cronExpression;
    private String serviceName;
    private String url;
    private String contentJson;
}
