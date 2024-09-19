package com.team11.delivery_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="user-service")
public interface UserFeignClient {
    @GetMapping("/api/users/getUserHubId/{username}")
    public UUID getUserHubId(@PathVariable("username") String username);

    @GetMapping("/api/users/getUserId/{username}")
    public Long getUserId(@PathVariable("username") String username);
}
