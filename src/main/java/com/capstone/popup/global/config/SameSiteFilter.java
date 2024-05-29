package com.capstone.popup.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SameSiteFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletResponse instanceof HttpServletResponse httpServletResponse) {
            String setCookieHeader = httpServletResponse.getHeader("Set-Cookie");
            if (setCookieHeader != null) {
                httpServletResponse.setHeader("Set-Cookie", setCookieHeader + "; SameSite=None; Secure");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
