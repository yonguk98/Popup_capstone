package com.capstone.popup.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OcrRequest {

    //    @Value("${ncp.clova.url}")
//    private String clovaApiUrl;
//
//    @Value("${ncp.clova.secret}")
//    private String secretKey;
    private final YmlUtil ymlUtil;

    public List<String> sendRequestToClova(Object jsonBody) {
        String clovaApiUrl = ymlUtil.getClovaUrl();
        String secretKey = ymlUtil.getClovaSecret();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) //timeout 시간 조절
                .responseTimeout(Duration.ofMillis(7000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient webClient = WebClient.builder()
                .baseUrl(clovaApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-OCR-SECRET", secretKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        Mono<String> response = webClient.post()
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class);

        String resultString = response.block();
        System.out.println(resultString);
//        response.subscribe(result -> {
//            resultString = result;
//        });

        return filtering(resultString);

    }

    public List<String> filtering(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            StringBuilder sb = new StringBuilder();

            JsonNode imagesNode = rootNode.get("images");
            if (imagesNode != null && imagesNode.isArray()) {
                for (JsonNode imageNode : imagesNode) {
                    JsonNode fieldsNode = imageNode.get("fields");
                    if (fieldsNode != null && fieldsNode.isArray()) {
                        for (JsonNode fieldNode : fieldsNode) {
                            JsonNode inferTextNode = fieldNode.get("inferText");
                            if (inferTextNode != null) {
                                String inferText = inferTextNode.asText();
                                sb.append(" " + inferTextNode.asText());
//                                System.out.println("Infer Text: " + inferText);
                            }
                        }
                    }
                }
            }
            return Arrays.stream(sb.toString().split("(\\d{1,2}\\.)\\s")).toList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String makeRequestBodyJson(String url) {
//        JSONObject body = new JSONObject();
//        body.put("lang", "ko");
//        body.put("requestId", UUID.fromString("ef2ded8a-90c2-4523-8fdd-635e1d20d0e6"));
//        body.put("resultType", "string");
//        body.put("timestamp", LocalDateTime.now());
//        body.put("version", "V1");
//
//
//        JSONObject image = new JSONObject();
//        image.put("format", "png");
//        image.put("name", "test1");
//        image.put("data", null);
//        image.put("url", url);
//
//        JSONArray imgArr = new JSONArray();
//        imgArr.add(image);
//
//        body.put("images", imgArr);
//        return body.toJSONString();

        String timestamp = "0";

        String jsonTemplate = """
                {
                    "lang": "ko",
                    "requestId": "%s",
                    "resultType": "string",
                    "timestamp": "%s",
                    "version": "V1",
                    "images": [
                        {
                            "format": "png",
                            "name": "test1",
                            "data": null,
                            "url": "%s"
                        }
                    ]
                }
                """;
        return String.format(jsonTemplate, UUID.fromString("ef2ded8a-90c2-4523-8fdd-635e1d20d0e6"), timestamp, url);
    }
}
