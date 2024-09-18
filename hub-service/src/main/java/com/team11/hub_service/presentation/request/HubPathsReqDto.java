package com.team11.hub_service.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HubPathsReqDto {

    @NotBlank
    private UUID startHubId;   // 출발 허브 ID
    private UUID endHubId;     // 도착 허브 ID
    private String path; //이동 경로 전시명
    private LocalTime duration; // 소요시간
    //private List<UUID> path;   // 이동경로 (허브 이동경로를 허브 UUID 리스트로 표현)

    public static HubPathsReqDto create(UUID startHubId, UUID endHubId, String path, LocalTime duration) {
        return HubPathsReqDto.builder()
                .startHubId(startHubId)
                .endHubId(endHubId)
                .path(path)
                .duration(duration)
                .build();
    }
}
