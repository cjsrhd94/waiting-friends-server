package com.example.waitinguserservice.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String SECRET = "a6894769aa24941d4ae869fc69d30ddd60cdbbce7b85d7cd1f2660e406b4ea037e443ac47d058a2a00364831a13bbafde895d6a42e0648f1b352c226197b077d";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Long ACCESS_EXPIRATION_TIME = 60 * 60 * 1000L;
    public static final String REFRESH_TOKEN_CACHE_KEY = "refreshToken:";
    public static final Long REFRESH_EXPIRATION_TIME = 30 * 24 * 60 * 60 * 1000L;

    public static final String EMAIL = "email";
    public static final String ROLE = "role";

    public String getEmail(String token) {
        return parsePayload(token)
                .get(EMAIL, String.class);
    }

    public String getRole(String token) {
        return parsePayload(token)
                .get(ROLE, String.class);
    }

    public Boolean isExpired(String token) {
        return parsePayload(token)
                .getExpiration().before(new Date());
    }

    public String createJwt(Long expiredMs) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(generateSecretKey())
                .compact();
    }

    public String createJwt(String email, String role, Long expiredMs) {
        return Jwts.builder()
                .claim(EMAIL, email)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(generateSecretKey())
                .compact();
    }

    private SecretKeySpec generateSecretKey() {
        return new SecretKeySpec(
                SECRET.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    private Claims parsePayload(String token) {
        return Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
