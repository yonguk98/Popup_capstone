package com.capstone.popup.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "사용할 아이디를 입력해주세요.")
    @Size(min = 4, max = 15)
    private String loginId;

    @NotNull(message = "사용할 비밀번호를 입력해주세요")
    @Size(min = 4, max = 15)
    private String loginPassword;

    @NotNull(message = "이메일을 입력해주세요")
    private String email;

    @NotNull(message = "사용할 닉네임을 입력해주세요")
    @Size(min = 2, max = 20)
    private String nickname;

//    @SuppressWarnings("JpaAttributeTypeInspection")
    public List<String> getAuthoritiesAsStrList() {
        if(loginId.equals("admin")){
            return List.of("ROLE_MEMBER", "ROLE_ADMIN");
        }
        return List.of("ROLE_MEMBER");
    }
}
