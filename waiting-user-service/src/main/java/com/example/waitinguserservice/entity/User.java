package com.example.waitinguserservice.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @Tsid
    private Long id;

    private String email;

    private String password;

    private String role;

    @Builder
    public User(
            String email,
            String password,
            String role
    ) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
