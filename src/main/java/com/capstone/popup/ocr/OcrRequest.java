package com.capstone.popup.ocr;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OcrRequest {

    public Mono<String> sendRequestToClova(Object jsonBody) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000) //timeout 시간 조절
                .responseTimeout(Duration.ofMillis(7000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        // TODO: application 파일로 이동
        String clovaApiUrl = "https://a515vys7wx.apigw.ntruss.com/custom/v1/26783/358771f2854c34bf294efba41a4cefcd8ebce4146656b32996971365a62b0d95/general";
        String secretKey = "TkdlYXFMYWRoa3hHVlJPbkVIUGJQV3VOUUVKUmlWR2w=";
        WebClient webClient = WebClient.builder()
                .baseUrl(clovaApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-OCR-SECRET", secretKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return webClient.post()
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    public String makeRequestBodyJson(String url){
        JSONObject body = new JSONObject();
        body.put("version","V1");
        body.put("lang","ko");
        body.put("timestamp", LocalDateTime.now());
        body.put("requestId", UUID.fromString("?"));


        JSONObject image = new JSONObject();
        image.put("name","");
        image.put("format","png");
        image.put("url",url);

        JSONArray imgArr = new JSONArray();
        imgArr.add(image);

        body.put("image",imgArr);

        return body.toJSONString();
    }
}
