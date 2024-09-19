package com.team11.hub_service.application.dto;

import com.team11.hub_service.domain.model.HubPaths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PathResultsDto {
    private UUID hubPathsId;

    private UUID startHubId;   // 출발 허브 ID
    private UUID endHubId;     // 도착 허브 ID
    private LocalTime duration; // 소요시간
    private String path; //이동 경로 전시명

    public static PathResultsDto from(HubPaths hubPaths) {
        return PathResultsDto.builder()
                .hubPathsId(hubPaths.getHubPathsId())
                .startHubId(hubPaths.getStartHubId().getHubId())
                .endHubId(hubPaths.getEndHubId().getHubId())
                .duration(hubPaths.getDuration())
                .path(hubPaths.getPath())
                .build();
    }
}
