package com.capstone.popup.global.jwt;

import com.capstone.popup.member.service.MemberLoginService;
import com.capstone.popup.member.util.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberLoginService memberLoginService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        log.info("JwtAuthenticationFilter 실행 : {}", LocalDateTime.now());
        // 요청 헤더에서 Authorization 헤더를 가져옴
        String authorizationHeader = request.getHeader("Authorization");
        String apiKey = null;

        // Authorization 헤더가 존재하고 "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            apiKey = authorizationHeader.substring(7); // "Bearer " 이후의 JWT를 추출
        }

        // apiKey가 존재하면 사용자 인증 설정
        if (apiKey != null) {
            SecurityUser user = memberLoginService.getUserFromApiKey(apiKey);
            if (user == null) {
                throw new RuntimeException("토큰이 잘못되었습니다.");
            }
            setAuthentication(user, request);
        }

        // 필터 체인 실행
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(SecurityUser user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
