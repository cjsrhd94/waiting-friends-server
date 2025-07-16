package com.example.waitinguserservice.dto.request;


import com.example.waitinguserservice.entity.User;
import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .role("ROLE_USER")
                .build();
    }
}
