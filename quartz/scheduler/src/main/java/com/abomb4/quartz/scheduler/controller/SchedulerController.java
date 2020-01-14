package com.abomb4.quartz.scheduler.controller;

import com.abomb4.quartz.common.*;
import com.abomb4.quartz.scheduler.MicroServiceExecutionJob;
import com.abomb4.quartz.scheduler.QuartzInformation;
import com.abomb4.quartz.scheduler.dto.*;
import lombok.extern.slf4j.*;
import org.quartz.*;
import org.quartz.impl.matchers.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.quartz.impl.matchers.GroupMatcher.*;

/**
 * 定时任务 API 。
 *
 * 系统已经预设了一些定时任务，这个 API 对已有定时任务进行操作，功能不包括添加和删除，包含：
 * <ul>
 *     <li>查询列表</li>
 *     <li>修改（expression、备注，名称没必要改）</li>
 *     <li>手工触发</li>
 *     <li>停用启用</li>
 *     <li>查询执行记录</li>
 * </ul>
 *
 * 系统对定时任务有核心定义，但对外只展示要展示的部分。
 *
 * @author abomb4 2020-01-10
 */
@RestController
@Slf4j
public class SchedulerController {
    private static String JOB_GROUP_NAME = "THE_JOB_GROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "THE_TRIGGER_GROUP_NAME";

    @Autowired
    private Scheduler scheduler;

    @PostMapping(value = "/scheduler/create")
    public StandardResponse<Boolean> create(@RequestBody ScheduleCreateRequest request) {
        final String jobName = "job";

        final JobDataMap paramMap = new JobDataMap();
        paramMap.put("url", request.getUrl());
        paramMap.put("contentJson", request.getContentJson());
        paramMap.put("serviceName", request.getServiceName());
        final JobDetail jobDetail = JobBuilder.newJob(MicroServiceExecutionJob.class)
                .withIdentity(jobName,JOB_GROUP_NAME)
                // .setJobData(paramMap)
                .build();

        try {
            // 触发器
            final CronScheduleBuilder cb = CronScheduleBuilder.cronSchedule(request.getCronExpression());
            final TriggerKey triggerKey = new TriggerKey(jobName, TRIGGER_GROUP_NAME);

            final Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)
                    .forJob(jobDetail)
                    .usingJobData(paramMap)
                    .withSchedule(cb)
                    .build();
            if (scheduler.getTrigger(triggerKey) != null) {
                log.warn("任务 [{}] 已经存在，先删除计划，再重新计划", triggerKey);
                scheduler.unscheduleJobs(List.of(triggerKey));
            }
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            return StandardResponse.success(true);
        } catch (ObjectAlreadyExistsException e) {
            log.debug("Exists", e);
            return StandardResponse.fail(EnumCommonRestResponseCode.AlreadyExists, "该定时任务已经存在");
        } catch (Exception e) {
            return StandardResponse.fail(EnumCommonRestResponseCode.Error, e.getMessage());
        }
    }

    @PostMapping(value = "/scheduler/remove")
    public StandardResponse<Boolean> remove(@RequestBody ScheduleRemoveRequest request) {
        try {
            final boolean unscheduled = scheduler.unscheduleJob(new TriggerKey(request.getId(), TRIGGER_GROUP_NAME));

            return StandardResponse.success(unscheduled);
        } catch (Exception e) {
            return StandardResponse.fail(EnumCommonRestResponseCode.Error, e.getMessage());
        }
    }

    @GetMapping("/scheduler/info")
    public QuartzInformation getSchedulerInformation() throws SchedulerException {
        final JobDetail build = JobBuilder.newJob(MicroServiceExecutionJob.class).build();

        SchedulerMetaData schedulerMetaData = scheduler.getMetaData();
        QuartzInformation quartzInformation = new QuartzInformation();
        quartzInformation.setVersion(schedulerMetaData.getVersion());
        quartzInformation.setSchedulerName(schedulerMetaData.getSchedulerName());
        quartzInformation.setInstanceId(schedulerMetaData.getSchedulerInstanceId());
        quartzInformation.setThreadPoolClass(schedulerMetaData.getThreadPoolClass());
        quartzInformation.setNumberOfThreads(schedulerMetaData.getThreadPoolSize());
        quartzInformation.setSchedulerClass(schedulerMetaData.getSchedulerClass());
        quartzInformation.setClustered(schedulerMetaData.isJobStoreClustered());
        quartzInformation.setJobStoreClass(schedulerMetaData.getJobStoreClass());
        quartzInformation.setNumberOfJobsExecuted(schedulerMetaData.getNumberOfJobsExecuted());
        quartzInformation.setInStandbyMode(schedulerMetaData.isInStandbyMode());
        quartzInformation.setStartTime(schedulerMetaData.getRunningSince());
        // enumerate each trigger group
        for(String group: scheduler.getTriggerGroupNames()) {
            // enumerate each trigger in group
            for(TriggerKey triggerKey : scheduler.getTriggerKeys(groupEquals(group))) {
                System.out.println("Found trigger identified by: " + triggerKey);
            }
        }

        for (String groupName : scheduler.getJobGroupNames()) {
            List<String> simpleJobList = new ArrayList<>();
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                Date nextFireTime = triggers.get(0).getNextFireTime();
                Date lastFireTime = triggers.get(0).getPreviousFireTime();
                simpleJobList.add(String.format("%1s.%2s - next run: %3s (previous run: %4s)", jobGroup, jobName, nextFireTime, lastFireTime));
            }
            quartzInformation.setSimpleJobDetail(simpleJobList);
        }
        return quartzInformation;
    }
}
