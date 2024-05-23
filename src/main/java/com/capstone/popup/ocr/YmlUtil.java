package com.capstone.popup.ocr;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class YmlUtil {

    @Value("${ncp.clova.url}")
    private String clovaUrl;

    @Value("${ncp.clova.secret}")
    private String clovaSecret;

    @Value("${crawling.insta.id}")
    private String instaId;

    @Value("${crawling.insta.password}")
    private String instaPwd;

    public String getClovaUrl() {
        return clovaUrl;
    }

    public String getClovaSecret() {
        return clovaSecret;
    }

}
