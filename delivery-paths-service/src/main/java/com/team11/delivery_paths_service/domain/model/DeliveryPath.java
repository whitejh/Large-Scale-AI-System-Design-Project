package com.team11.delivery_paths_service.domain.model;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqeunce_number")
    private long sequence;

    @Column(name="delivery_id")
    private UUID deliveryId;

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
}
