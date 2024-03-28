package com.capstone.popup.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingService {

    private final Scheduler scheduler;
    private final CrawlingJobTriggerService triggerService;
    private final CrawlingJobDetailService detailService;

    public void registerCrawlingJob(){

    }

    public void updateCrawlingJob(){

    }

    public void deleteCrawlingJob(){

    }

}
