package com.example.waitinguserservice.dto.request;


import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
}
