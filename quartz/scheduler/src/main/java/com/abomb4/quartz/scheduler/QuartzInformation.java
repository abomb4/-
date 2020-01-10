package com.abomb4.quartz.scheduler;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QuartzInformation {
    private String version;
    private String schedulerName;
    private String instanceId;
    private Class threadPoolClass;
    private int numberOfThreads;
    private Class schedulerClass;
    private boolean isClustered;
    private Class jobStoreClass;
    private long numberOfJobsExecuted;
    private Date startTime;
    private boolean inStandbyMode;
    private List<String> simpleJobDetail;

    public String getSchedulerProductName() {
        return "Quartz Scheduler (spring-boot-starter-quartz)";
    }
}
