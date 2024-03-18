package com.capstone.popup.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberMypqgeUpdateRequestDto {
    private String nickname;
    private String loginPassword1;
    private String loginPassword2;
}
