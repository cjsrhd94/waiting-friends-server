package com.example.waitinguserservice.service;

import com.example.waitinguserservice.dto.request.SignUpRequest;
import com.example.waitinguserservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void singUp(SignUpRequest request) {
        // validate
        checkDuplicateEmail(request.getEmail());

        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }
}
