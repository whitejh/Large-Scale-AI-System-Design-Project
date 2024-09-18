package com.team11.hub_service.application.service;

import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.domain.model.Hub;
import com.team11.hub_service.domain.repository.HubRepository;
//import com.team11.hub_service.infrastructure.feign.UserFeignClient;
import com.team11.hub_service.infrastructure.feign.UserFeignClient;
import com.team11.hub_service.presentation.request.HubRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    private final UserFeignClient userClient;

    public String getUserInfo(String userId) {
        return userClient.getUser(userId);
    }

    // 허브 생성
    public HubResponseDto createHub(HubRequestDto req) {
        Hub hub = HubRequestDto.toHub(req);
        return HubResponseDto.from(hubRepository.save(hub));
    }

    // 허브 수정
    @Transactional
    public HubResponseDto updateHub(HubRequestDto requestDto, UUID hubId) {
        // 해당 허브 조회
        Hub hub = hubRepository.findByHubIdAndDeleteFalse(hubId)
                .orElseThrow(() -> new IllegalArgumentException("수정하려는 허브가 존재하지 않습니다."));

        hub.updateHub(requestDto);
        return HubResponseDto.from(hubRepository.save(hub));
    }

    // 허브 상세 조회
    public HubResponseDto readHub(UUID hubId) {
        Hub hub = hubRepository.findByHubIdAndDeleteFalse(hubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 허브를 조회하지 못했습니다."));
        return HubResponseDto.from(hub);
    }

    // 허브 전체 조회
    public List<HubResponseDto> readAllHub(UUID hubId, Pageable pageable) {
        Page<Hub> hubList = hubRepository.findByHubIdAndDeleteFalseOrderByCreatedAtDesc(hubId, pageable)
                .orElseThrow(() -> new IllegalArgumentException("허브 목록를 조회하지 못했습니다."));

        return hubList.stream().map(HubResponseDto::from).collect(Collectors.toList());
    }

    // 허브 검색
    public HubResponseDto searchHub(String hubName) {
        Hub hub = hubRepository.findByNameContainingAndDeleteFalse(hubName)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 허브를 찾을 수 없습니다 : " + hubName));
        return HubResponseDto.from(hub);
    }


    // 허브 삭제
    @Transactional
    public void deleteHub(UUID hubId, String userName) {
        Hub hub = hubRepository.findByHubIdAndDeleteFalse(hubId)
                        .orElseThrow(() -> new IllegalArgumentException("삭제하려는 허브가 존재하지 않습니다."));

        hub.setDelete(true); // 허브 삭제시 delete 필드를 true로 설정
        hub.setDeleted(new Timestamp(System.currentTimeMillis()), userName);
        hubRepository.save(hub);
    }

    // 업체 생성 및 수정될 때
    // 허브 id가 실제로 존재하는 허브인지 확인
    public boolean isHubIdExist(UUID hudId) {
        return hubRepository.findByHubIdAndDeleteFalse(hudId).isPresent(); // true/false
    }

    //////////////////////////////////////////////////////////////////////////////////
    // findHub 메서드 /////
    private Hub findHub(UUID hubId) {
        return hubRepository.findByHubIdAndDeleteFalse(hubId).orElseThrow(() ->
                new IllegalArgumentException("선택한 허브는 존재하지 않습니다."));
    }

}
