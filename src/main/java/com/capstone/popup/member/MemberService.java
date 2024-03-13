package com.capstone.popup.member;

import com.capstone.popup.member.dto.MemberCreateRequestDto;
import com.capstone.popup.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberCreateRequestDto dto){
        // 로그인 아이디 중복 확인
        validDuplicateLoginId(dto.getLoginId());

        // 로그인 비밀번호 일치 확인
        passwordEqualCheck(dto.getLoginPassword1(), dto.getLoginPassword2());

        // Member 엔티티 생성
        Member newMember = Member.builder()
                .loginId(dto.getLoginId())
                .loginPassword(passwordEncoder.encode(dto.getLoginPassword2()))
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .build();

        // DB에 저장
        memberRepository.save(newMember);
    }

    private void validDuplicateLoginId(String loginId){
        memberRepository.findByLoginId(loginId).ifPresent(result -> {
            // TODO:커스텀 예외로 바꾸기?
            throw new RuntimeException("아이디 중복");
        });
    }

    private void passwordEqualCheck(String password1, String password2){
        if(!password1.equals(password2)){
            // TODO:커스텀 예외로 바꾸기?
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
