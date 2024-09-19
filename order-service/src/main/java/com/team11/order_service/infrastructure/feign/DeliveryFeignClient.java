package com.team11.order_service.infrastructure.feign;

import com.team11.order_service.application.dto.OrderToDeliveryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name="delivery-service")
public interface DeliveryFeignClient {
    @PostMapping("api/deliveries/createDelivery")
    public UUID createDelivery(@RequestBody OrderToDeliveryDto dto);

    @GetMapping("/api/deliveries/getDriverId/{deliveryId}")
    public Long getDriverId(@PathVariable("deliveryId") UUID deliveryId);
}
