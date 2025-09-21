package com.example.waitinguserservice.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReissueRequest {
    private String refreshToken;

    public ReissueRequest(
            String refreshToken
    ) {
        this.refreshToken = refreshToken;
    }
}
