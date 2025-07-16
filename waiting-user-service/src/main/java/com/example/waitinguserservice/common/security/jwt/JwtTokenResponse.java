package com.example.waitinguserservice.common.security.jwt;

import lombok.Builder;
import lombok.Data;

@Data
public class JwtTokenResponse {
    private String grantType;
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private Long refreshExpiresIn;

    @Builder
    public JwtTokenResponse(
            String grantType,
            String accessToken,
            Long expiresIn,
            String refreshToken,
            Long refreshExpiresIn
    ) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshExpiresIn = refreshExpiresIn;
    }
}
