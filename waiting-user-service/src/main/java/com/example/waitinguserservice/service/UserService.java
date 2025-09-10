package com.example.waitinguserservice.service;

import com.example.waitingredis.util.RedisUtil;
import com.example.waitinguserservice.common.exception.InvalidRefreshTokenException;
import com.example.waitinguserservice.common.security.jwt.JwtTokenResponse;
import com.example.waitinguserservice.common.security.jwt.JwtUtil;
import com.example.waitinguserservice.dto.request.LogoutRequest;
import com.example.waitinguserservice.dto.request.ReissueRequest;
import com.example.waitinguserservice.dto.request.SignUpRequest;
import com.example.waitinguserservice.dto.response.UserResponse;
import com.example.waitinguserservice.entity.User;
import com.example.waitinguserservice.repository.UserRepository;
import com.example.waitinguserservice.repository.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waitinguserservice.common.security.jwt.JwtUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserReader userReader;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidator signUpValidator;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Transactional
    public void singUp(SignUpRequest request) {
        signUpValidator.validate(request.getEmail(), request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        log.info("Signing up user with email: {}", user.getEmail());

        userRepository.save(user);
    }

    @Transactional
    public JwtTokenResponse reissue(ReissueRequest request) {
        String tokenValue = request.getRefreshToken();

        if (!jwtUtil.validateToken(tokenValue)) {
            throw new InvalidRefreshTokenException();
        }

        String email = jwtUtil.getEmail(tokenValue);

        if (!redisUtil.getData(REFRESH_TOKEN_CACHE_KEY + email).equals(tokenValue)) {
            throw new InvalidRefreshTokenException();
        }

        User user = userReader.findByEmail(email);

        String accessToken = jwtUtil.createJwt(user.getId(), user.getEmail(), user.getRole(), ACCESS_EXPIRATION_TIME);
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
            throw new InvalidRefreshTokenException();
        }

        String email = userDetails.getUsername();

        // redis에 저장된 토큰과 비교
        if (!redisUtil.getData(REFRESH_TOKEN_CACHE_KEY + email).equals(tokenValue)) {
            throw new InvalidRefreshTokenException();
        }

        redisUtil.deleteData(REFRESH_TOKEN_CACHE_KEY + email);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(String email) {
        User user = userReader.findByEmail(email);
        return new UserResponse(user.getId(), user.getEmail());
    }
}
