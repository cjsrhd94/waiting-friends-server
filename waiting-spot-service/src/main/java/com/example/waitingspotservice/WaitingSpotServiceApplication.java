package com.example.waitingspotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WaitingSpotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitingSpotServiceApplication.class, args);
    }

}
