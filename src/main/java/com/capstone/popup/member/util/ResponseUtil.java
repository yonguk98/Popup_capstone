package com.capstone.popup.member.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public void addHeader(String header, String value) {
        response.addHeader(header, value);
    }
}
