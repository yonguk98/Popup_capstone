package com.capstone.popup.member.controller;

import com.capstone.popup.global.GlobalResponse;
import com.capstone.popup.member.dto.MemberLoginResponseDto;
import com.capstone.popup.member.service.MemberLoginService;
import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.member.dto.MemberCreateRequestDto;
import com.capstone.popup.member.dto.MemberLoginRequestDto;
import com.capstone.popup.member.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberLoginService memberLoginService;
    private final CookieUtil cookieUtil;

    @PostMapping("/create")
    public GlobalResponse memberCreate(@Valid @RequestBody MemberCreateRequestDto dto){
        memberService.createMember(dto);
        return GlobalResponse.of("200","회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public GlobalResponse memberLogin(@Valid @RequestBody MemberLoginRequestDto dto){
        MemberLoginResponseDto responseDto = memberLoginService.Login(dto);
        cookieUtil.addCrossDomainCookie(responseDto.getAccessToken(), responseDto.getRefreshToken());
        return GlobalResponse.of("200","");
    }

    @PostMapping("/logout")
    public GlobalResponse logout() {
        cookieUtil.removeCrossDomainCookie();
        return GlobalResponse.of("200", "로그아웃 성공");
    }

}
