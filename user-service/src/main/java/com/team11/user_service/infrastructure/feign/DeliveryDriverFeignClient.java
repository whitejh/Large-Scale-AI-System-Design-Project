package com.team11.user_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient("deliveryDriver-service")
public interface DeliveryDriverFeignClient {

    @PutMapping("/api/drivers/slackUpdate/{userId}/{slackId}")
    UUID updateSlackId(@PathVariable(name = "userId") Long userId, @PathVariable(name = "slackId") UUID slackId);
}