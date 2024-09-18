package com.team11.hub_service.application.dto;


import com.team11.hub_service.domain.model.HubPaths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HubPathsRespDto {

    private UUID hubPathsId;

    private HubResponseDto startHubId;   // 출발 허브 ID
    private HubResponseDto endHubId;     // 도착 허브 ID
    private LocalTime duration; // 소요시간
    private String path; //이동 경로 전시명

    private String createdBy;
    private String updatedBy;
    private String deletedBy;

    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static HubPathsRespDto fromEntity(HubPaths hubPaths) {
        return HubPathsRespDto.builder()
                .hubPathsId(hubPaths.getHubPathsId())
                .startHubId(HubResponseDto.from(hubPaths.getStartHubId()))
                .endHubId(HubResponseDto.from(hubPaths.getEndHubId()))
                .path(hubPaths.getPath())
                .duration(hubPaths.getDuration())
                .createdAt(hubPaths.getCreatedAt())
                .updatedAt(hubPaths.getUpdatedAt())
                .deletedAt(hubPaths.getDeletedAt())
                .build();
    }
}
