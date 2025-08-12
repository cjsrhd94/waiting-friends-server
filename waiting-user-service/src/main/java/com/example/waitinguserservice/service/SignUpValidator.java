package com.example.waitinguserservice.service;

import com.example.waitinguserservice.common.exception.AlreadyExistEmailException;
import com.example.waitinguserservice.common.exception.EmailNotEmptyException;
import com.example.waitinguserservice.common.exception.InvalidPasswordFormatException;
import com.example.waitinguserservice.common.exception.PasswordNotEmptyException;
import com.example.waitinguserservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpValidator {
    private final UserRepository userRepository;

    public void validate(String email, String password) {
        checkEmail(email);
        checkPassword(password);
    }

    private void checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new EmailNotEmptyException();
        }

        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistEmailException("이미 사용중인 이메일입니다: " + email);
        }
    }

    private void checkPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new PasswordNotEmptyException();
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new InvalidPasswordFormatException("비밀번호는 8자 이상 20자 이하로 입력해야 합니다.");
        }

        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            throw new InvalidPasswordFormatException("비밀번호는 영문자와 숫자를 모두 포함해야 합니다.");
        }
    }
}
