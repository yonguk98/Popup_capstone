package com.capstone.popup.member.service;

import com.capstone.popup.member.dto.MemberMypageResponseDto;
import com.capstone.popup.member.dto.MemberMypqgeUpdateRequestDto;
import com.capstone.popup.member.repository.MemberRepository;
import com.capstone.popup.member.dto.MemberCreateRequestDto;
import com.capstone.popup.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Struct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberCreateRequestDto dto) {
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

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));
    }

    public MemberMypageResponseDto getMemberMypqgeData(String loginId) {
        Member loginMember = getMemberByLoginId(loginId);

        return MemberMypageResponseDto.builder()
                .loginId(loginMember.getLoginId())
                .nickname(loginMember.getNickname())
                .email(loginMember.getEmail())
                .build();
    }

    public void updateMypageData(String loginId, MemberMypqgeUpdateRequestDto dto) {
        Member loginMember = getMemberByLoginId(loginId);

        // 닉네임 널 체크
        if (!dto.getNickname().isEmpty()) {
            loginMember = loginMember.toBuilder().nickname(dto.getNickname()).build();
        }
        // 비밀번호 널 체크
        if (!dto.getLoginPassword1().isEmpty() && !dto.getLoginPassword2().isEmpty()) {
            passwordEqualCheck(dto.getLoginPassword1(), dto.getLoginPassword2());
            loginMember = loginMember.toBuilder().loginPassword(passwordEncoder.encode(dto.getLoginPassword2())).build();
        }

        memberRepository.save(loginMember);
    }

    private Member getMemberByLoginId(String loginId) {
        Optional<Member> memberOp = memberRepository.findByLoginId(loginId);

        if (memberOp.isEmpty()) {
            throw new RuntimeException("해당 아이디와 일치하는 회원을 찾을 수 없습니다.");
        }

        return memberOp.get();
    }

    private void validDuplicateLoginId(String loginId) {
        memberRepository.findByLoginId(loginId).ifPresent(result -> {
            // TODO:커스텀 예외로 바꾸기?
            throw new RuntimeException("아이디 중복");
        });
    }

    private void passwordEqualCheck(String password1, String password2) {
        if (!password1.equals(password2)) {
            // TODO:커스텀 예외로 바꾸기?
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
