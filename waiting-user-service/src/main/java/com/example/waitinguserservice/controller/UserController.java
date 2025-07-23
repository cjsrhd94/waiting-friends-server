package com.example.waitinguserservice.controller;

import com.example.waitinguserservice.common.security.jwt.JwtTokenResponse;
import com.example.waitinguserservice.dto.request.LogoutRequest;
import com.example.waitinguserservice.dto.request.ReissueRequest;
import com.example.waitinguserservice.dto.request.SignUpRequest;
import com.example.waitinguserservice.dto.response.UserResponse;
import com.example.waitinguserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Service is running");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
        userService.singUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtTokenResponse> reissue(
            @RequestBody ReissueRequest request
    ) {
        return ResponseEntity.ok(userService.reissue(request));
    }

    @PostMapping("/logout")
    public  ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LogoutRequest request
    ) {
        userService.logout(userDetails,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }
}
