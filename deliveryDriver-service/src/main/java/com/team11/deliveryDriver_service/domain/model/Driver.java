package com.team11.deliveryDriver_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_delivery_drivers")
public class Driver extends BaseEntity {
    @Id
    @Column(name="user_id")
    private Long userId;

    @Column(name="driver_type")
    @Enumerated(value= EnumType.STRING)
    private DriverTypeEnum type;

    @Column(name="slack_id")
    private UUID slackId;

    @Column(name="hub_id")
    private UUID hubId;


}
