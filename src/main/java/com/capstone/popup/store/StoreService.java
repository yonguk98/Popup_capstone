package com.capstone.popup.store;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private static String resultString;

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
        System.out.println(1);
        WebClient webClient = WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", "hucjy7m1er")
                .defaultHeader("X-NCP-APIGW-API-KEY", "3khmL2HuhvASK6hpI9Nz7TqT1JGaS7RGzsC8NP6Q")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        System.out.println(2);
        Mono<String> response = webClient.get()
                .uri("?query=" + addr)
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(response);
        System.out.println(3);

        resultString = response.block();
//        response.subscribe(result -> {
//            System.out.println(result);
//            resultString = result;
//        });
        System.out.println(4);
        System.out.println(resultString);

        return resultString;
    }
}
