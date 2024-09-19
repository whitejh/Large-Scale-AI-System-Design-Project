package com.team11.order_service.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name="product-service")
public interface ProductFeignClient {
    @GetMapping("/api/products/stock/{productId}")
    int getStockByProductId(@PathVariable("productId") UUID productId);

    @PutMapping("/api/products/stockUpdate/{productId}/{stock}")
    void updateStockByProductId(@PathVariable("productId") UUID productId, @PathVariable(name="stock") int stock);
}
