package com.example.waitingreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WaitingReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaitingReservationServiceApplication.class, args);
	}

}
