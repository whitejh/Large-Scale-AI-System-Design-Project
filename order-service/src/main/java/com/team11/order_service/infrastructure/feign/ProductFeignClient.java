package com.team11.order_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name="product-service")
public interface ProductFeignClient {
    @GetMapping("/products/stock/{productId}")
    int getStockByProductId(@PathVariable("productId") UUID productId);

    @PutMapping("/products/stock/{productId}")
    void updateStockByProductId(@PathVariable("productId") UUID productId, int stock);
}
