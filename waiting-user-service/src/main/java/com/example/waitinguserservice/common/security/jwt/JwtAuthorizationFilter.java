package com.example.waitinguserservice.common.security.jwt;

import com.example.waitinguserservice.entity.User;
import com.example.waitinguserservice.repository.reader.UserReader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static com.example.waitinguserservice.common.security.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.example.waitinguserservice.common.security.jwt.JwtUtil.BEARER_PREFIX;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserReader userReader;
    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            UserReader userReader,
            JwtUtil jwtUtil
    ) {
        super(authenticationManager);
        this.userReader = userReader;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // Authorization 헤더가 없거나, Bearer로 시작하지 않으면 다음 필터로 넘어간다.
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 Bearer 접두사를 제거하고, 토큰을 추출한다.
        String accessToken = request.getHeader(AUTHORIZATION_HEADER)
                .replace(BEARER_PREFIX, "");

        try {
            String email = jwtUtil.getEmail(accessToken);
            User user = userReader.findByEmail(email);

            // 시큐리티 내 권한 처리를 위해 'UsernamePasswordAuthenticationToken' 을 만들고, Authentication 객체를 만든다.
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            // Authentication 객체를 시큐리티 세션에 저장한다.
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

        chain.doFilter(request, response);
    }
}
