package com.capstone.popup.store;

import com.capstone.popup.admin.AdminArticleService;
import com.capstone.popup.ocr.Crawling;
import com.capstone.popup.ocr.OcrRequest;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final AdminArticleService adminArticleService;
    private static String resultString;
    private final OcrRequest ocrRequest;

    @Value("${ncp.map.client.id}")
    String clientId;
    @Value("${ncp.map.client.secret}")
    String clientSecret;

    public void registerStore(StoreCreateRequestDto dto) {
        Store store = Store.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .build();

        checkDuplicationStore(dto.getName());

        storeRepository.save(store);
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("스토어를 찾을 수 없습니다."));
    }

    public List<Store> getAllStore() {
        // TODO: paging 추가 필요
        return storeRepository.findAll();
    }

    public void updateStoreById(Long id, StoreCreateRequestDto dto) {
        Store store = getStoreById(id);

        store.toBuilder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .location(dto.getLocation())
                .build();

        storeRepository.save(store);
    }

    public void deleteStoreById(Long id) {
        storeRepository.deleteById(id);
    }

    private void checkDuplicationStore(String name) {
        storeRepository.findByName(name).ifPresent(store -> {
                    throw new EntityExistsException("이미 존재하는 스토어 입니다.");
                }
        );
    }

    public String requestGeocode(String addr){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) //timeout 시간 조절
                .responseTimeout(Duration.ofMillis(7000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        WebClient webClient = WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        Mono<String> response = webClient.get()
                .uri("?query=" + addr)
                .retrieve()
                .bodyToMono(String.class);

        resultString = response.block();

        return resultString;
    }

    public void crawlingRun(String accountName){
        Crawling crawling = new Crawling();
//        OcrRequest ocrRequest = new OcrRequest();

        List<String> urlList = crawling.run(accountName);
        log.info(accountName + "크롤링 완료");

        List<String> ocrResponse = new ArrayList<>();
        for(String url : urlList){
            String jsonBody = ocrRequest.makeRequestBodyJson(url);
            System.out.println(jsonBody);
            ocrResponse = ocrRequest.sendRequestToClova(jsonBody);
            log.info("ocr 요청 완료");
            ocrResponse.forEach(adminArticleService::createArticle);
            // 번호로 구분된 결과들을 각각 게시글로 생성
            log.info("게시글 생성 완료");
        }

    }
}
