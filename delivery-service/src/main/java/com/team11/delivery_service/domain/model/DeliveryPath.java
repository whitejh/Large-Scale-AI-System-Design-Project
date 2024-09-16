package com.team11.delivery_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_delivery_paths")
public class DeliveryPath extends BaseEntity {
    @Id
    @Column(name="delivery_id")
    private UUID deliveryId;

    @Column(name="sequence_number")
    private int sequenceNumber;

    @Column(name="origin_hub_id")
    private UUID originHubId;

    @Column(name="destination_hub_id")
    private UUID destinationHubId;

    @Column(name="estimated_distance")
    private float estimatedDistance;

    @Column(name="estimated_duration")
    private LocalTime estimatedDuration;

    @Column(name="actual_distance")
    private float actualDistance;

    @Column(name="actual_duration")
    private LocalTime actualDuration;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private DeliveryStatusEnum status;

    public DeliveryPath from(Delivery delivery, int sequence) {
        return DeliveryPath.builder()
                .deliveryId(delivery.getDeliveryId())
                .sequenceNumber(sequence)
                .originHubId(delivery.getOriginHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .status(delivery.getStatus())
                .build();
    }
}
