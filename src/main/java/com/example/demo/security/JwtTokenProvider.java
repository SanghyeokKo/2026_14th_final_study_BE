package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityTime = 1000 * 60 * 60; // 토큰 유효시간: 1시간

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. 로그인 성공 시 토큰(통행증) 발급
    public String createToken(String email, String role) {
        // 0.12.x 스타일의 Claims 빌더 패턴 적용
        Claims claims = Jwts.claims()
                .subject(email)
                .add("role", role)
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityTime);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key) // 0.12.x에서는 알고리즘을 키 크기에 맞춰 자동으로 지정해줍니다.
                .compact();
    }

    // 2. 통행증에서 이메일 꺼내기
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key) // setSigningKey 대신 verifyWith 사용
                .build()
                .parseSignedClaims(token) // parseClaimsJws 대신 parseSignedClaims 사용
                .getPayload() // getBody 대신 getPayload 사용
                .getSubject();
    }

    // 3. 위조되거나 만료된 통행증이 아닌지 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}