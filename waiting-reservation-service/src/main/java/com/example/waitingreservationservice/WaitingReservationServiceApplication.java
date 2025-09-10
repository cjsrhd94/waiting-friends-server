package com.example.waitingreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(scanBasePackages = {
		"com.example.waitingreservationservice",
		"com.example.waitingcommon",
		"com.example.waitingredis"
})
@EnableFeignClients
@EnableRetry
public class WaitingReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaitingReservationServiceApplication.class, args);
	}

}
