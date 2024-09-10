package com.team11.delivery_service.presentation.request;

import com.team11.delivery_service.domain.model.Delivery;
import com.team11.delivery_service.domain.model.DeliveryStatusEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReqDto {
    private UUID deliveryId;
    private DeliveryStatusEnum status;
    private UUID originHubId;
    private UUID destinationHubId;
    private String deliveryAddress;
    private String recipientName;
    private UUID recipientSlackId;

    public static Delivery toDelivery(DeliveryReqDto reqDto) {
        return Delivery.builder()
                .deliveryId(reqDto.getDeliveryId())
                .status(reqDto.getStatus())
                .originHubId(reqDto.getOriginHubId())
                .destinationHubId(reqDto.getDestinationHubId())
                .deliveryAddress(reqDto.getDeliveryAddress())
                .recipientName(reqDto.getRecipientName())
                .recipientSlackId(reqDto.getRecipientSlackId())
                .build();
    }

}
