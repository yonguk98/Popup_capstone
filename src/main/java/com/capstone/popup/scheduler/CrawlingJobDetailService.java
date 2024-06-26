package com.capstone.popup.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@RequiredArgsConstructor
@Component
public class CrawlingJobDetailService {

    public JobDetail build(JobKey jobKey, String accountName, Integer weekDay){

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("accountName", accountName);
        jobDataMap.put("weekDay", weekDay);

        return newJob(CrawlingJob.class)
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .storeDurably(true)
                .usingJobData(jobDataMap)
                .build();
    }
}
