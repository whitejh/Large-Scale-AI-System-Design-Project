package com.team11.hub_service.application.service;


import com.team11.hub_service.application.dto.HubPathsRespDto;
import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.application.dto.PathResultsDto;
import com.team11.hub_service.domain.model.Hub;
import com.team11.hub_service.domain.model.HubPaths;
import com.team11.hub_service.domain.repository.HubPathsRepository;
import com.team11.hub_service.domain.repository.HubRepository;
import com.team11.hub_service.presentation.request.HubPathsReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubPathsService {

    private final HubRepository hubRepository;
    private final HubPathsRepository hubPathsRepository;

    @Transactional
    public HubPathsRespDto createHubPaths(HubPathsReqDto reqDto) {
        Hub startHub = hubRepository.findByHubIdAndDeleteFalse(reqDto.getStartHubId())
                .orElseThrow(() -> new IllegalArgumentException("허브를 찾지 못했습니다."));

        Hub endHub = hubRepository.findByHubIdAndDeleteFalse(reqDto.getEndHubId())
                .orElseThrow(() -> new IllegalArgumentException("허브를 찾지 못했습니다."));

        HubPaths hubPaths = HubPaths.create(startHub, endHub, reqDto.getPath(), reqDto.getDuration());

        return HubPathsRespDto.fromEntity(hubPathsRepository.save(hubPaths));
    }

    // 허브 경로 수정
    @Transactional
    public HubPathsRespDto updateHubPaths(HubPathsReqDto reqDto, UUID hubPathsId) {
        HubPaths hubPaths =  hubPathsRepository.findById(hubPathsId)
                .orElseThrow(() -> new IllegalArgumentException("수정하려는 허브 경로를 찾지 못했습니다."));

        // 출발 허브
        Hub startHub = null;
        if(reqDto.getStartHubId() != null) {
            startHub = hubRepository.findByHubIdAndDeleteFalse(reqDto.getStartHubId())
                    .orElseThrow(() -> new IllegalArgumentException("허브를 찾지 못했습니다."));
        }

        // 도착 허브
        Hub endHub = null;
        if(reqDto.getStartHubId() != null) {
            endHub = hubRepository.findByHubIdAndDeleteFalse(reqDto.getStartHubId())
                    .orElseThrow(() -> new IllegalArgumentException("허브를 찾지 못했습니다."));
        }

        hubPaths.update(startHub, endHub, reqDto);
        return HubPathsRespDto.fromEntity(hubPaths);
    }

    // 허브 경로 검색
    public HubPathsRespDto searchHubPaths(String path) {
        HubPaths hubPaths = hubPathsRepository.findBypathContainingAndDeleteFalse(path)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 허브 경로를 찾을 수 없습니다 : " + path));
        return HubPathsRespDto.fromEntity(hubPaths);
    }


    // 허브 경로 단건 조회
    @Transactional(readOnly = true)
    public HubPathsRespDto findHubPaths(UUID hubPathsId) {
        HubPaths hubPaths =  hubPathsRepository.findById(hubPathsId)
                .orElseThrow(() -> new IllegalArgumentException("허브 경로를 찾지 못했습니다."));

        return HubPathsRespDto.fromEntity(hubPaths);
    }

    // 허브 경로 전체 조회
    @Transactional(readOnly = true)
    public List<HubPathsRespDto> findAllHubPaths(UUID hubPathsId, Pageable pageable) {
        Page<HubPaths> hubPathsList = hubPathsRepository.findByHubPathsIdAndDeleteFalseOrderByCreatedAtDesc(hubPathsId, pageable)
            .orElseThrow(() -> new IllegalArgumentException("허브 목록를 조회하지 못했습니다."));

        return hubPathsList.stream().map(HubPathsRespDto::fromEntity).collect(Collectors.toList());
    }

    // 허브 경로 삭제
    @Transactional
    public void deleteHubPaths(UUID hubPathsId) {
        HubPaths hubPaths =  hubPathsRepository.findById(hubPathsId)
                .orElseThrow(() -> new IllegalArgumentException("허브 경로를 찾지 못했습니다."));

        hubPaths.setDelete(true); // 허브 삭제시 delete 필드를 true로 설정
        hubPaths.setDeleted(new Timestamp(System.currentTimeMillis()));
        hubPathsRepository.save(hubPaths);
    }

    // 허브 간 이동 경로 반환
    public List<PathResultsDto> getHubPaths(UUID startHubId, UUID endHubId) {
        List<HubPaths> hubPaths = hubPathsRepository.findAllByDeleteIsFalse().orElseThrow(
                ()-> new IllegalArgumentException("허브 간 이동 경로 정보를 가져오는데 실패했습니다.")
        );

        // 출발 허브 ID를 기준으로 아래로 내려가는지 위로 올라가는지 찾기
        int startIndex = hubPaths.indexOf(hubPaths.stream().filter(hubPath -> hubPath.getStartHubId().getHubId().equals(startHubId)).findFirst().get());
        log.info(startHubId.toString());
        int endIndex = hubPaths.indexOf(hubPaths.stream().filter(hubPath -> hubPath.getStartHubId().getHubId().equals(endHubId)).findFirst().get());
        log.info(endHubId.toString());

        // 위 인덱스 기준 출발 허브 인덱스가 도착 허브 인덱스보다 작으면 위에서 아래로 경로 찾기.
        // 출발 허브 인덱스가 도착 허브 인덱스보다 크면 아래에서 위로 경로 찾기.
        List<HubPaths> pathResults = new ArrayList<>();

        if( startIndex < endIndex ) {
            endIndex = hubPaths.indexOf(hubPaths.stream().filter(hubPath -> hubPath.getEndHubId().getHubId().equals(endHubId)).findFirst().get());

            pathResults = hubPaths.subList(startIndex, endIndex);
        }else if( startIndex > endIndex ) {
            startIndex = hubPaths.indexOf(hubPaths.stream().filter(hubPath -> hubPath.getEndHubId().getHubId().equals(startHubId)).findFirst().get());
            endIndex = hubPaths.indexOf(hubPaths.stream().filter(hubPath -> hubPath.getStartHubId().getHubId().equals(endHubId)).findFirst().get());

            pathResults = hubPaths.subList(endIndex, startIndex);
            Collections.reverse(pathResults);
        }else {
            // 인덱스가 같을 경우 허브 간 이동 없이 허브에서 바로 업체 배송
        }

        return pathResults.stream().map(PathResultsDto::from).collect(Collectors.toList());
    }
}
