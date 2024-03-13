package com.capstone.popup.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MemberCreateRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String loginPassword1;

    @NotBlank
    private String loginPassword2;

    @NotBlank
    private String email;

    @NotEmpty
    private String nickname;

}
