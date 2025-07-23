package com.example.waitinguserservice.service;

import com.example.waitinguserservice.common.security.jwt.JwtTokenResponse;
import com.example.waitinguserservice.common.security.jwt.JwtUtil;
import com.example.waitinguserservice.common.util.RedisUtil;
import com.example.waitinguserservice.dto.request.LogoutRequest;
import com.example.waitinguserservice.dto.request.ReissueRequest;
import com.example.waitinguserservice.dto.request.SignUpRequest;
import com.example.waitinguserservice.dto.response.UserResponse;
import com.example.waitinguserservice.entity.User;
import com.example.waitinguserservice.repository.UserRepository;
import com.example.waitinguserservice.repository.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waitinguserservice.common.security.jwt.JwtUtil.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserReader userReader;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

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
    public JwtTokenResponse reissue(ReissueRequest request) {
        String tokenValue = request.getRefreshToken();

        if (!jwtUtil.validateToken(tokenValue)) {
            throw new RuntimeException();
        }

        String email = jwtUtil.getEmail(tokenValue);

        if (!redisUtil.getData(REFRESH_TOKEN_CACHE_KEY + email).equals(tokenValue)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String accessToken = jwtUtil.createJwt(user.getEmail(), user.getRole(), ACCESS_EXPIRATION_TIME);
        String resRefreshToken = jwtUtil.createJwt(user.getEmail(), REFRESH_EXPIRATION_TIME);

        redisUtil.setData(REFRESH_TOKEN_CACHE_KEY + email, resRefreshToken, REFRESH_EXPIRATION_TIME);

        return JwtTokenResponse.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .expiresIn(ACCESS_EXPIRATION_TIME)
                .refreshToken(resRefreshToken)
                .refreshExpiresIn(REFRESH_EXPIRATION_TIME)
                .build();
    }

    @Transactional
    public void logout(UserDetails userDetails, LogoutRequest request) {
        String tokenValue = request.getRefreshToken();

        if (!jwtUtil.validateToken(tokenValue)) {
            throw new RuntimeException();
        }

        String email = userDetails.getUsername();

        // redis에 저장된 토큰과 비교
        if (!redisUtil.getData(REFRESH_TOKEN_CACHE_KEY + email).equals(tokenValue)) {
            throw new IllegalArgumentException("로그아웃 실패: 유효하지 않은 토큰입니다.");
        }

        redisUtil.deleteData(REFRESH_TOKEN_CACHE_KEY + email);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(String email) {
        User user = userReader.findByEmail(email);
        return new UserResponse(user.getId(), user.getEmail());
    }
}
