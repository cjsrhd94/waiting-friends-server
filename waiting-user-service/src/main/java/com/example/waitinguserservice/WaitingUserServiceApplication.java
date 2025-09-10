package com.example.waitinguserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.waitinguserservice",
        "com.example.waitingcommon",
        "com.example.waitingredis"
})
public class WaitingUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitingUserServiceApplication.class, args);
    }

}
