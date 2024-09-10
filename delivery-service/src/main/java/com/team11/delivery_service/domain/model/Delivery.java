package com.team11.delivery_service.domain.model;

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
    @Column(name="delivery_id")
    private UUID deliveryId;

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

    @Column(name="is_deleted")
    private boolean deleted;
}
