package com.team11.order_service.presentation.request;

import com.team11.order_service.domain.model.Order;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    private UUID orderId;
    private UUID deliveryId;
    private UUID productId;
    private int quantity;
    private UUID supplyCompany;
    private UUID receiveCompany;

    public static Order toOrder(OrderReqDto reqDtoDto) {
        return Order.builder()
                .orderId(reqDtoDto.getOrderId())
                .deliveryId(reqDtoDto.getDeliveryId())
                .productId(reqDtoDto.getProductId())
                .quantity(reqDtoDto.getQuantity())
                .supplyCompanyId(reqDtoDto.getSupplyCompany())
                .receiveCompanyId(reqDtoDto.getReceiveCompany())
                .build();
    }
}
