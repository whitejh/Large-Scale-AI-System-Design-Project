package com.team11.product_service.presentation.request;

import com.team11.product_service.domain.model.Product;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReqDto {
    private UUID productId;
    private String productName;
    private UUID companyId;
    private UUID hubId;
    private int stock;

    public static Product toProduct(ProductReqDto reqDto) {
        return Product.builder()
                .productName(reqDto.getProductName())
                .companyId(reqDto.getCompanyId())
                .hubId(reqDto.getHubId())
                .stock(reqDto.getStock())
                .build();
    }
}
