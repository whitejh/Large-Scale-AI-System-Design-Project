package com.team11.hub_service.presentation.controller;

import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.application.service.HubService;
import com.team11.hub_service.presentation.request.HubRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name="Hub", description="Hub API")
@RestController
@RequestMapping("/hubs")
@Slf4j
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    // 허브 생성
    @Operation(summary="허브 추가", description="허브를 추가합니다.")
    @PostMapping
    public ResponseEntity<HubResponseDto> createHub (@RequestBody HubRequestDto requestDto) {
        return ResponseEntity.ok(hubService.createHub(requestDto));
    }

    // 허브 수정
    @Operation(summary="허브 수정", description="허브의 정보를 수정합니다.")
    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> updateHub(@RequestBody HubRequestDto requestDto, @PathVariable UUID hubId){
        return ResponseEntity.ok(hubService.updateHub(requestDto, hubId));
    }


    // 허브 조회
    @Operation(summary="허브 상세 조회", description="특정 허브를 조회합니다.")
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> readHub(@PathVariable UUID hubId){
        return ResponseEntity.ok(hubService.readHub(hubId));
    }

    // 허브 검색
    @Operation(summary="허브 검색", description="특정 허브를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<HubResponseDto> searchHub(@RequestParam String hubName) {

        return ResponseEntity.ok(hubService.searchHub(hubName));
    }

    // 허브 삭제
    @Operation(summary="허브 삭제", description="특정 허브를 삭제합니다.")
    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId, @RequestHeader(name="X-User-Name", required = false) String userName) {
        hubService.deleteHub(hubId, userName);
        return ResponseEntity.noContent().build();
    }

    // 업체 생성 및 수정될 때 허브 id가 실제로 존재하는 허브인지 확인
    // 존재하면 true, 안하면 false
    @GetMapping("/company/{hubId}")
    public boolean getCompanyByHubId(@PathVariable UUID hubId) {
        return hubService.getCompany(hubId); // true / false
    }



}
