package com.capstone.popup.member.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MemberCreateRequestDto {

    @NotBlank(message = "사용할 아이디를 입력해주세요.")
    @Size(min = 4, max = 15)
    private String loginId;

    @NotBlank(message = "사용할 비밀번호를 입력해주세요")
    @Size(min = 4, max = 15)
    private String loginPassword1;

    @NotBlank(message = "사용할 비밀번호를 입력해주세요")
    @Size(min = 4, max = 15)
    private String loginPassword2;

    @NotBlank
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="이메일 주소 양식을 확인해주세요")
    private String email;

    @NotEmpty(message = "사용할 닉네임을 입력해주세요")
    @Size(min = 2, max = 20)
    private String nickname;

}
