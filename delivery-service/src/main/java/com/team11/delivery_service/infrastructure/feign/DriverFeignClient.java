package com.team11.delivery_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name="deliveryDriver-service")
public interface DriverFeignClient {
    @GetMapping("/api/drivers/getHubDrivers")
    public List<Long> getHubDrivers();

    @GetMapping("/api/drivers/getCompanyDrivers/{hubId}")
    public List<Long> getCompanyDrivers(@PathVariable("hubId") UUID hubId);
}
