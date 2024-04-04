package com.capstone.popup.scheduler;

import com.capstone.popup.ocr.Crawling;
import com.capstone.popup.ocr.OcrRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@DisallowConcurrentExecution // 중복 실행 방지
@PersistJobDataAfterExecution
public class CrawlingJob implements Job {

    private Crawling crawling;
    private OcrRequest ocrRequest;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String accountName = jobDataMap.getString("accountName");

        List<String> urlList = crawling.run(accountName);
        String jsonBody = ocrRequest.makeRequestBodyJson(urlList.get(1));
        List<String> ocrResponse = ocrRequest.sendRequestToClova(jsonBody);

    }
}
