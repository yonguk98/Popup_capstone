package com.capstone.popup.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingService {

    private final Scheduler scheduler;
    private final CrawlingJobTriggerService triggerService;
    private final CrawlingJobDetailService detailService;

    public void registerCrawlingJob(String accountName, Integer weekDay){
        JobKey jobKey = makeJobKey(accountName);

        // check job key duplication

        // job detail, trigger build
        JobDetail jobDetail = detailService.build(jobKey, accountName, weekDay);
        Trigger trigger = triggerService.build(jobKey, weekDay);
        // enroll job to scheduler
        enrollJobToScheduler(jobDetail, trigger);
    }

    public void updateCrawlingJob(){

    }

    public void deleteCrawlingJob(){

    }

    private JobKey makeJobKey(String accountName){
        JobKey key = JobKey.jobKey(accountName);

        log.info("Created jobkey: ", key.getName());
        return key;
    }

    private void enrollJobToScheduler(JobDetail jobDetail, Trigger trigger) {
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

}
