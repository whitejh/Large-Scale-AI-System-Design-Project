package com.team11.product_service.application.dto;

import com.team11.product_service.domain.model.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRespDto {
    private UUID productId;
    private String productName;
    private UUID companyId;
    private UUID hubId;
    private int stock;

    public static ProductRespDto from(Product product) {
        return ProductRespDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .stock(product.getStock())
                .build();
    }
}
