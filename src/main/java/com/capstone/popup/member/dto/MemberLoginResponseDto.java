package com.capstone.popup.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponseDto {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
}
