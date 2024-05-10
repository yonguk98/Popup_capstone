package com.capstone.popup.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberMypageResponseDto {
    private String loginId;
    private String nickname;
    private String email;
}
