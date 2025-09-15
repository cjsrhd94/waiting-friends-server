package com.example.waitingmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.waitingmessage",
        "com.example.waitingkafka",
})
public class WaitingMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitingMessageApplication.class, args);
    }

}
