package com.abomb4.quartz.scheduler.service;

import com.abomb4.quartz.common.PageResponse;
import com.abomb4.quartz.common.StandardResponse;
import com.abomb4.quartz.scheduler.core.TaskCoreDefinition;
import com.abomb4.quartz.scheduler.core.TaskExecuteLog;
import com.abomb4.quartz.scheduler.dto.TaskExecuteLogQueryRequest;
import com.abomb4.quartz.scheduler.dto.TaskExecuteRequest;
import com.abomb4.quartz.scheduler.dto.TaskQueryRequest;
import com.abomb4.quartz.scheduler.dto.TaskUpdateRequest;

/**
 * 定时任务服务
 * <p>
 * 系统已经预设了一些定时任务，这个 API 对已有定时任务进行操作，功能不包括添加和删除，包含：
 * <ul>
 *     <li>查询列表</li>
 *     <li>修改（expression、备注，名称没必要改）</li>
 *     <li>手工触发</li>
 *     <li>停用启用</li>
 *     <li>查询执行记录</li>
 * </ul>
 *
 * @author yangrl14628 2020-01-14
 */
public interface ScheduleService {

    /**
     * 查询任务列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    StandardResponse<PageResponse<TaskCoreDefinition>> queryList(TaskQueryRequest query);

    /**
     * 更新任务信息
     *
     * @param request 更新请求
     * @return 影响条数
     */
    StandardResponse<Integer> update(TaskUpdateRequest request);

    /**
     * 手动发起
     *
     * @param request 手动发起请求，允许替换自定义参数的
     * @return 是否开始了执行
     */
    StandardResponse<Boolean> manualExecute(TaskExecuteRequest request);

    /**
     * 停用某任务
     *
     * @param id id
     * @return 若早已停用，返回 false ，否则 true
     */
    StandardResponse<Boolean> disable(Long id);

    /**
     * 启用某任务
     *
     * @param id id
     * @return 若早已启用，返回 false ，否则 true
     */
    StandardResponse<Boolean> enable(Long id);

    /**
     * 查询执行记录列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    StandardResponse<PageResponse<TaskExecuteLog>> queryExecuteLogList(TaskExecuteLogQueryRequest query);
}
