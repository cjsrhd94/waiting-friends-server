package com.example.waitingeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class WaitingEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaitingEurekaApplication.class, args);
    }

}
