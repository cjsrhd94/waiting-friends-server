package com.example.waitinguserservice.common.security.jwt;

import com.example.waitinguserservice.common.util.RedisUtil;
import com.example.waitinguserservice.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.example.waitinguserservice.common.security.jwt.JwtUtil.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createJwt(username, role, ACCESS_EXPIRATION_TIME);
        String refreshToken = jwtUtil.createJwt(username, REFRESH_EXPIRATION_TIME);

        redisUtil.setData(REFRESH_TOKEN_CACHE_KEY + username, refreshToken, REFRESH_EXPIRATION_TIME);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        JwtTokenResponse tokenResponse = JwtTokenResponse.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .expiresIn(ACCESS_EXPIRATION_TIME)
                .refreshToken(refreshToken)
                .refreshExpiresIn(REFRESH_EXPIRATION_TIME)
                .build();

        response.getWriter()
                .print(new JSONObject(tokenResponse));
    }

    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}