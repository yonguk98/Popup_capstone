package com.capstone.popup.member.controller;

import com.capstone.popup.global.GlobalResponse;
import com.capstone.popup.member.dto.*;
import com.capstone.popup.member.service.MemberLoginService;
import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.member.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberLoginService memberLoginService;
    private final CookieUtil cookieUtil;

    @PostMapping("/create")
    public GlobalResponse memberCreate(@Valid @RequestBody MemberCreateRequestDto dto) {
        memberService.createMember(dto);
        return GlobalResponse.of("200", "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public GlobalResponse memberLogin(@Valid @RequestBody MemberLoginRequestDto dto) {
        MemberLoginResponseDto responseDto = memberLoginService.Login(dto);
        cookieUtil.addCrossDomainCookie(responseDto.getAccessToken(), responseDto.getRefreshToken());
        return GlobalResponse.of("200", "로그인 성공", responseDto);
    }

    @PostMapping("/logout")
    public GlobalResponse logout() {
        cookieUtil.removeCrossDomainCookie();
        return GlobalResponse.of("200", "로그아웃 성공");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public GlobalResponse getMypage(Principal principal) {
        MemberMypageResponseDto responseDto = memberService.getMemberMypqgeData(getName(principal));

        return GlobalResponse.of("200", "마이페이지 접속", responseDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public GlobalResponse updateMypageData(Principal principal, @RequestBody MemberMypqgeUpdateRequestDto dto) {
        memberService.updateMypageData(getName(principal), dto);
        return GlobalResponse.of("200", "회원 정보 변경 완료");
    }

    private String getName(Principal principal) {
        try {
            String loginId = principal.getName();
            return loginId;
        } catch (Exception e) {
            throw new RuntimeException("로그인 상태가 아닙니다.");
        }
    }

}
