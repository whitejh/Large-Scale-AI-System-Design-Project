package com.team11.delivery_service.domain.model;

import com.team11.delivery_service.application.dto.PathResultsDto;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public static DeliveryPath from(PathResultsDto dto, UUID deliveryId, int sequence, DeliveryStatusEnum status) {
        return DeliveryPath.builder()
                .deliveryId(deliveryId)
                .sequenceNumber(sequence)
                .originHubId(dto.getStartHubId())
                .destinationHubId(dto.getEndHubId())
                .actualDuration(dto.getDuration())
                .status(status)
                .build();
    }
}
