package com.capstone.popup.scheduler;

import com.capstone.popup.admin.AdminArticleService;
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
    @Autowired
    private AdminArticleService adminArticleService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String accountName = jobDataMap.getString("accountName");

        List<String> urlList = crawling.run(accountName);
        log.info(accountName + "크롤링 완료");

        String jsonBody = ocrRequest.makeRequestBodyJson(urlList.get(1));
        List<String> ocrResponse = ocrRequest.sendRequestToClova(jsonBody);
        log.info("ocr 요청 완료");

        // 번호로 구분된 결과들을 각각 게시글로 생성
        ocrResponse.stream().forEach(data -> adminArticleService.createArticle(data));
        log.info("게시글 생성 완료");
    }
}
