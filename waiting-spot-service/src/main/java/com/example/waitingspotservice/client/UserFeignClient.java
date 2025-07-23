package com.example.waitingspotservice.client;

import com.example.waitingspotservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${spot.server.user}", path = "/api/v1/users")
public interface UserFeignClient {

    @GetMapping
    UserResponse getUser(@RequestParam("email") String email);
}
