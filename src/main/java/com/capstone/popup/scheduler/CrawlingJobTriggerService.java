package com.capstone.popup.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import java.text.ParseException;

import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class CrawlingJobTriggerService {

    public Trigger build(JobKey jobKey, Integer weekDay){

        CronExpression cronExpression = makeCronExpression(weekDay);
        if (cronExpression == null) {
            // null 처리, 커스텀 예외 필요
            throw new NullPointerException("크론표현식이 존재하지 않습니다.");
        }
        return newTrigger()
                .forJob(jobKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // 정기 결제 주기마다 실행을 시킨다.
                .withIdentity(new TriggerKey(jobKey.getName(), jobKey.getGroup()))
                .startNow()
                .build();
    }

    private CronExpression makeCronExpression(Integer weekDay) {
        try {
            CronExpression cronExpression = new CronExpression(String.format("0 15 11 ? * %s *", weekDay));
            return cronExpression;
        } catch (ParseException e) {
            // exception 처리 추가 필요
            throw new RuntimeException(e);
        }
    }

    public Trigger update(Scheduler scheduler, JobKey jobKey, Integer weekDay){
        // 트리거 키 생성
        TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());

        // cron trigger 형태로 불러오기?? 이게 가능?
        CronTriggerImpl trigger = new CronTriggerImpl();
        try{
            trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e){
            throw new RuntimeException();
            // 커스텀 예외
        }

        //크론 표현식 정의
        CronExpression cronExpression = makeCronExpression(weekDay);

        // 새로운 날짜로 트리거 업데이트
        trigger.setCronExpression(cronExpression);

        return trigger;
    }
}
