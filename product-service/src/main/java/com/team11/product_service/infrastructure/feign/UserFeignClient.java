package com.team11.product_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="user-service")
public interface UserFeignClient {

}
