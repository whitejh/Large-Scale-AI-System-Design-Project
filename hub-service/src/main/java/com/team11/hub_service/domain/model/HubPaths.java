package com.team11.hub_service.domain.model;

import com.team11.hub_service.presentation.request.HubPathsReqDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name="p_hub_paths")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access= AccessLevel.PUBLIC)
public class HubPaths extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_paths_id", updatable = false, nullable = false)
    private UUID hubPathsId;

    @OneToOne
    @JoinColumn(name = "start_hub_id")
    private Hub startHubId;    // 출발 허브 id

    @OneToOne
    @JoinColumn(name = "end_hub_id")
    private Hub endHubId;      // 도착 허브 id

    @Column(nullable = false)
    private LocalTime duration; // 소요시간

    @Column(nullable = false)
    private String path; //이동 경로 전시명

    @Column(name="is_deleted")
    private boolean delete = false;

    public static HubPaths create(Hub startHubId, Hub endHubId, String path, LocalTime duration) {
        return HubPaths.builder()
                .startHubId(startHubId)
                .endHubId(endHubId)
                .path(path)
                .duration(duration)
                .build();
    }

    public void update(Hub startHubId, Hub endHubId, HubPathsReqDto reqDto) {
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.path = reqDto.getPath();
        this.duration= reqDto.getDuration();
    }
}
