package com.capstone.popup.ocr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YmlUtil {

    @Value("${ncp.clova.url}")
    private String clovaUrl;

    @Value("${ncp.clova.secret}")
    private String clovaSecret;

    public String getClovaUrl() {
        return clovaUrl;
    }

    public String getClovaSecret() {
        return clovaSecret;
    }

}
