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

    public static Order toOrder(OrderReqDto redDto) {
        return Order.builder()
                .orderId(redDto.getOrderId())
                .deliveryId(redDto.getDeliveryId())
                .productId(redDto.getProductId())
                .quantity(redDto.getQuantity())
                .supplyCompanyId(redDto.getSupplyCompany())
                .receiveCompanyId(redDto.getReceiveCompany())
                .build();
    }
}
