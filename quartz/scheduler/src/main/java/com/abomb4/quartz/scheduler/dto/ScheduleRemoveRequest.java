package com.abomb4.quartz.scheduler.dto;

import lombok.*;

/**
 * 删除计划请求
 *
 * @author abomb4 2020-01-10
 */
@Data
public class ScheduleRemoveRequest {
    private EnumJobType jobType;

    private String id;
}
