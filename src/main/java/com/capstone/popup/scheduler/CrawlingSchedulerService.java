package com.capstone.popup.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingSchedulerService {

    private final Scheduler scheduler;
    private final CrawlingJobTriggerService triggerService;
    private final CrawlingJobDetailService detailService;

    public void registerCrawlingJob(String accountName, Integer weekDay) {
        JobKey jobKey = makeJobKey(accountName);

        // check job key duplication

        // job detail, trigger build
        JobDetail jobDetail = detailService.build(jobKey, accountName, weekDay);
        Trigger trigger = triggerService.build(jobKey, weekDay);
        // enroll job to scheduler
        enrollJobToScheduler(jobDetail, trigger);
    }


    public void deleteCrawlingJob(String accountName) {

        JobKey jobKey = makeJobKey(accountName);

        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            // TODO: 작업 삭제 실패시 다시시도? or 실패 알림
            throw new RuntimeException("작업을 삭제하는데 실패했습니다.");
        }

    }

    public void updateCrawlingJob(String accountName, Integer weekDay) {
        JobKey jobKey = makeJobKey(accountName);

        // TODO: 기존 작업 삭제하고 다시 등록하기? 아니면 있는 작업 찾아서 업데이트하기?
        deleteCrawlingJob(accountName);
        registerCrawlingJob(accountName, weekDay);

    }

    private JobKey makeJobKey(String accountName) {
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
