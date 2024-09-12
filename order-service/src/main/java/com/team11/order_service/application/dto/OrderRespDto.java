package com.team11.order_service.application.dto;

import com.team11.order_service.domain.model.Order;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRespDto {
    private UUID orderId;
    private UUID deliveryId;
    private UUID productId;
    private int quantity;
    private UUID supplyCompany;
    private UUID receiveCompany;

    public static OrderRespDto from(Order order) {
        return OrderRespDto.builder()
                .orderId(order.getOrderId())
                .deliveryId(order.getDeliveryId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .supplyCompany(order.getSupplyCompanyId())
                .receiveCompany(order.getReceiveCompanyId())
                .build();
    }


}
