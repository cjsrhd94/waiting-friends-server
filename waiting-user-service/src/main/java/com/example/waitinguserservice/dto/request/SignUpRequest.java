package com.example.waitinguserservice.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    private String email;
    private String password;

    public SignUpRequest(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }
}
