package com.team11.delivery_service.domain.model;

import com.team11.delivery_service.presentation.request.RecipientReqDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_deliveries")
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="delivery_id")
    private UUID deliveryId;

    @Column(name="hub_driver_id")
    private Long hubDriverId;

    @Column(name="company_driver_id")
    private Long companyDriverId;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private DeliveryStatusEnum status;

    @Column(name="origin_hub_id")
    private UUID originHubId;

    @Column(name="destination_hub_id")
    private UUID destinationHubId;

    @Column(name="delivery_address")
    private String deliveryAddress;

    @Column(name="recipient_name")
    private String recipientName;

    @Column(name="recipient_slack_id")
    private UUID recipientSlackId;

    public Delivery(Long hubDriverId, Long companyDriverId,UUID originHubId, UUID destinationHubId, String deliveryAddress, DeliveryStatusEnum status, String recipientName, UUID recipientSlackId){
        this.hubDriverId = hubDriverId;
        this.companyDriverId = companyDriverId;
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.recipientName = recipientName;
        this.recipientSlackId = recipientSlackId;
    }

    public void updateRecipient(RecipientReqDto reqDto){
        this.recipientName = reqDto.getRecipientName();
        this.recipientSlackId = reqDto.getRecipientSlackId();
    }
}
