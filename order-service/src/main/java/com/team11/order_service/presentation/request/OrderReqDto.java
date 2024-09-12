package com.team11.order_service.presentation.request;

import com.team11.order_service.domain.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    @NotBlank
    private UUID productId;
    @NotBlank(message="상품의 개수를 꼭 입력해주세요.")
    private int quantity;

    private UUID supplyCompany;
    private UUID receiveCompany;

    @NotBlank
    private String recipientName;
    @NotBlank
    private UUID recipientSlackId;

    public static Order toOrder(OrderReqDto reqDtoDto) {
        return Order.builder()
                .productId(reqDtoDto.getProductId())
                .quantity(reqDtoDto.getQuantity())
                .supplyCompanyId(reqDtoDto.getSupplyCompany())
                .receiveCompanyId(reqDtoDto.getReceiveCompany())
                .build();
    }
}
