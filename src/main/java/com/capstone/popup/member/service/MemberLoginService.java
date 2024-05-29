package com.capstone.popup.member.service;

import com.capstone.popup.global.jwt.JwtUtil;
import com.capstone.popup.member.dto.MemberLoginResponseDto;
import com.capstone.popup.member.repository.MemberRepository;
import com.capstone.popup.member.util.SecurityUser;
import com.capstone.popup.member.dto.MemberLoginRequestDto;
import com.capstone.popup.member.entity.Member;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Value("${jwt.secret})")
    private String SECRET_KEY;

    public SecurityUser getUserFromApiKey(String token) {
        // decoding jwt
        Claims claims = JwtUtil.decode(token, SECRET_KEY);

        // check token type
        String tokenType = claims.get("type", String.class);
        if (!"access".equals(tokenType) && !"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }

        if ("access".equals(tokenType)) {
            // get user data
            Map<String, Object> data = (Map<String, Object>) claims.get("data");
            long id = Long.parseLong((String) data.get("id"));
            String username = (String) data.get("loginId");
            List<? extends GrantedAuthority> authorities = ((List<String>) data.get("authorities"))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new SecurityUser(
                    id,
                    username,
                    "",
                    authorities
            );
        }
        return null;
    }

    public MemberLoginResponseDto Login(MemberLoginRequestDto dto) {
        Member loginMember = getAndCheckLoginIdAndPassword(dto.getLoginId(), dto.getPassword());

        String accessToken = createAccessToken(loginMember);
        String refreshToken = createRefreshToken(loginMember);

        return MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Member getAndCheckLoginIdAndPassword(String loginId, String password) {
        Optional<Member> memberOp = memberRepository.findByLoginId(loginId);

        if (memberOp.isEmpty()) {
            throw new RuntimeException("일치하는 회원이 없습니다.");
        }

        if (!encoder.matches(password, memberOp.get().getLoginPassword())) {
            throw new RuntimeException("password not matches");
        }

        return memberOp.get();
    }

    private String createAccessToken(Member member) {
        String accessToken = JwtUtil.encode(
                60 * 60, // 60 minute
                Map.of(
                        "id", member.getId().toString(),
                        "loginId", member.getLoginId(),
                        "authorities", member.getAuthoritiesAsStrList()
                )
                , SECRET_KEY
                , "access"
        );

        return accessToken;
    }

    private String createRefreshToken(Member member) {
        String refreshToken = JwtUtil.encode(
                60 * 60 * 24, //1 day
                Map.of(
                        "id", member.getId().toString(),
                        "loginId", member.getLoginId()
                )
                , SECRET_KEY
                , "refresh"
        );
//        memberRestService.setRefreshToken(member, refreshToken);

        return refreshToken;
    }

}
