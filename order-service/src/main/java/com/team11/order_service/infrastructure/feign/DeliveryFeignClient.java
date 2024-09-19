package com.team11.order_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name="delivery-service")
public interface DeliveryFeignClient {
    @PostMapping("api/deliveries/")
    public UUID createDelivery(UUID supplyCompanyId, UUID receiveCompanyId, String recipientName, UUID recipientSlackId, String userName);

    @GetMapping("/api/deliveries/getDriverId/{deliveryId}")
    public Long getDriverId(@PathVariable("deliveryId") UUID deliveryId);
}
