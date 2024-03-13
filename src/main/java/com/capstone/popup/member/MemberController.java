package com.capstone.popup.member;

import com.capstone.popup.global.GlobalResponse;
import com.capstone.popup.member.dto.MemberCreateRequestDto;
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

    @PostMapping("create")
    public GlobalResponse memberCreate(@Valid @RequestBody MemberCreateRequestDto dto){
        memberService.createMember(dto);
        return GlobalResponse.of("200","회원가입이 완료되었습니다.");
    }
}
