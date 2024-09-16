package com.team11.delivery_service.application.dto;

import com.team11.delivery_service.domain.model.Delivery;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRespDto {
    private UUID deliveryId;
    private DeliveryStatusEnum status;
    private UUID originHubId;
    private UUID destinationHubId;
    private String deliveryAddress;
    private String recipientName;
    private UUID recipientSlackId;

    public static DeliveryRespDto from(Delivery delivery) {
        return DeliveryRespDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .status(delivery.getStatus())
                .originHubId(delivery.getOriginHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipientName(delivery.getRecipientName())
                .recipientSlackId(delivery.getRecipientSlackId())
                .build();
    }
}
