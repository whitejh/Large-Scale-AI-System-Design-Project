package com.team11.hub_service.application.dto;

import com.team11.hub_service.domain.model.Hub;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HubResponseDto {

    private UUID hubId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    private String createdBy;
    private String updatedBy;
    private String deletedBy;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static HubResponseDto from(Hub hub) {
        return HubResponseDto.builder()
                .hubId(hub.getHubId())
                .name(hub.getName())
                .address(hub.getName())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .createdBy(hub.getCreatedBy())
                .updatedBy(hub.getUpdatedBy())
                .deletedBy(hub.getDeletedBy())
                .createdAt(hub.getCreatedAt())
                .updatedAt(hub.getUpdatedAt())
                .deletedAt(hub.getDeletedAt())
                .build();
    }

}
