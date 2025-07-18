package com.example.waitinguserservice.service;

import com.example.waitinguserservice.common.security.jwt.JwtUtil;
import com.example.waitinguserservice.dto.request.LogoutRequest;
import com.example.waitinguserservice.dto.request.SignUpRequest;
import com.example.waitinguserservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.waitinguserservice.common.security.jwt.JwtUtil.REFRESH_TOKEN_CACHE_KEY;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    @Transactional
    public void singUp(SignUpRequest request) {
        // validate
        checkDuplicateEmail(request.getEmail());

        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional
    public void logout(UserDetails userDetails, LogoutRequest request) {
        String tokenValue = request.getRefreshToken();

        if (!jwtUtil.validateToken(tokenValue)) {
            throw new RuntimeException();
        }

        String email = userDetails.getUsername();

        // redis에 저장된 토큰과 비교
        if (!getRefreshTokenByEmail(email).equals(tokenValue)) {
            throw new IllegalArgumentException("로그아웃 실패: 유효하지 않은 토큰입니다.");
        }

        redisTemplate.delete(REFRESH_TOKEN_CACHE_KEY + email);
    }

    @Transactional
    public String getRefreshTokenByEmail(String email) {
        return redisTemplate.opsForValue()
                .get(REFRESH_TOKEN_CACHE_KEY + email);
    }
}
