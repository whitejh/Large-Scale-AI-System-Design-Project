package com.team11.hub_service.application.service;


import com.team11.hub_service.application.dto.HubPathsRespDto;
import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.domain.model.Hub;
import com.team11.hub_service.domain.model.HubPaths;
import com.team11.hub_service.domain.repository.HubPathsRepository;
import com.team11.hub_service.domain.repository.HubRepository;
import com.team11.hub_service.presentation.request.HubPathsReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
}
