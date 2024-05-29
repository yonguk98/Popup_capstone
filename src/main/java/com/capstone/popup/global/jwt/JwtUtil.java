package com.capstone.popup.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    public static String encode(long expirationSeconds, Map<String, Object> data, String secretKey, String type) {
        Claims claims = Jwts
                .claims()
                .add("type", type)
                .subject("auth")
                .add("data", data)
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expirationSeconds);

        return Jwts.builder().claims(claims).issuedAt(now).expiration(expiration).signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static Claims decode(String token, String secretKey) {
        return Jwts
                .parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token)
                .getPayload();
    }
}
