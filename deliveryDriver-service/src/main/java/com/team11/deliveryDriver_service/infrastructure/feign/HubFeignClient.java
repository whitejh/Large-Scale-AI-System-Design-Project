package com.team11.deliveryDriver_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="hub-service")
public interface HubFeignClient {

    @GetMapping("/api/hubs/check/{hubId}")
    public boolean isHubIdExist(@PathVariable("hubId") UUID hubId);
}
