package com.team11.delivery_service.application.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderToDeliveryDto {
    private UUID supplyCompanyId;
    private UUID receiveCompanyId;
    private String recipientName;
    private UUID recipientSlackId;
    private String userName;
}
