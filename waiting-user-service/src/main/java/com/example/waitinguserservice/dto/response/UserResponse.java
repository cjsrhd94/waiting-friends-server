package com.example.waitinguserservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserResponse {
    private Long userId;
    private String email;

    public UserResponse(
            Long userId,
            String email
    ) {
        this.userId = userId;
        this.email = email;
    }
}
