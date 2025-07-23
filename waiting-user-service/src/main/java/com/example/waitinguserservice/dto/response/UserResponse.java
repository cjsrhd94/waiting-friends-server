package com.example.waitinguserservice.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String email;

    public UserResponse(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
