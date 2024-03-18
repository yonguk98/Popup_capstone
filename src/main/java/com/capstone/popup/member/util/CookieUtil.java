package com.capstone.popup.member.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public void addCrossDomainCookie(String accessToken, String refreshToken){
        ResponseCookie cookie1 = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .maxAge(60 * 60) // 60 minutes
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        ResponseCookie cookie2 = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(60 * 60 * 24) // 1day
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie1.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
    }

    public void removeCrossDomainCookie() {
        ResponseCookie cookie1 = ResponseCookie.from("accessToken", null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        ResponseCookie cookie2 = ResponseCookie.from("refreshToken", null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", cookie1.toString());
        response.addHeader("Set-Cookie", cookie2.toString());
    }

    public void getRefreshTokenCookie(){
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new NoSuchElementException("쿠키가 존재하지 않습니다.");
        }

        Optional<Cookie> refreshTokenCookieOp = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst();

        if (refreshTokenCookieOp.isEmpty()) {
           throw new NoSuchElementException("리프레시 토큰 쿠키가 존재하지 않습니다.");
        }

        String refreshToken = refreshTokenCookieOp.get().getValue();
        //TODO: 리프레시 토큰으로 회원 찾아서 토큰 재생성하고 쿠키에 담아서 전달
    }

}
