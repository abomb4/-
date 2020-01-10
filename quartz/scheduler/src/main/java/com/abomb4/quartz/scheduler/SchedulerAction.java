package com.abomb4.quartz.scheduler;

import com.abomb4.quartz.common.*;
import com.abomb4.quartz.scheduler.dto.*;
import lombok.extern.slf4j.*;
import org.quartz.*;
import org.quartz.impl.matchers.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.quartz.impl.matchers.GroupMatcher.*;

/**
 *
 *
 * @author abomb4 2020-01-10
 */
@RestController
@Slf4j
public class SchedulerAction {
    private static String JOB_GROUP_NAME = "THE_JOB_GROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "THE_TRIGGER_GROUP_NAME";

    @Autowired
    private Scheduler scheduler;

    @PostMapping(value = "/scheduler/create")
    public RestResponse<Boolean> create(@RequestBody ScheduleCreateRequest request) {
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
            return RestResponse.success(true);
        } catch (ObjectAlreadyExistsException e) {
            log.debug("Exists", e);
            return RestResponse.fail(EnumCommonRestResponseCode.AlreadyExists, "该定时任务已经存在");
        } catch (Exception e) {
            return RestResponse.fail(EnumCommonRestResponseCode.Error, e.getMessage());
        }
    }

    @PostMapping(value = "/scheduler/remove")
    public RestResponse<Boolean> remove(@RequestBody ScheduleRemoveRequest request) {
        try {
            final boolean unscheduled = scheduler.unscheduleJob(new TriggerKey(request.getId(), TRIGGER_GROUP_NAME));

            return RestResponse.success(unscheduled);
        } catch (Exception e) {
            return RestResponse.fail(EnumCommonRestResponseCode.Error, e.getMessage());
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
