package com.team11.hub_service.presentation.controller;

import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.application.service.HubService;
import com.team11.hub_service.presentation.request.HubRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Hub", description="Hub API")
@RestController
@RequestMapping("/hubs")
@Slf4j
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    // 권한 -> MASTER
    @Operation(summary="허브 추가", description="허브를 추가합니다.")
    @PostMapping
    @PreAuthorize("hasRole('Master')")
    public ResponseEntity<HubResponseDto> createHub (@RequestBody HubRequestDto requestDto) {
        return ResponseEntity.ok(hubService.createHub(requestDto));
    }

    // 권한 -> MASTER
    @Operation(summary="허브 수정", description="허브의 정보를 수정합니다.")
    @PutMapping("/{hubId}")
    @PreAuthorize("hasRole('Master')")
    public ResponseEntity<HubResponseDto> updateHub(@RequestBody HubRequestDto requestDto, @PathVariable UUID hubId){
        return ResponseEntity.ok(hubService.updateHub(requestDto, hubId));
    }

    @Operation(summary="허브 상세 조회", description="특정 허브를 조회합니다.")
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDto> readHub(@PathVariable UUID hubId){
        return ResponseEntity.ok(hubService.readHub(hubId));
    }

    @Operation(summary="허브 전체 조회", description="전체 허브를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<HubResponseDto>> readHub(@PathVariable UUID hubId,
                                                        @PageableDefault(size=10) Pageable pageable,
                                                        @RequestParam(name="size", required = false) Integer size){
        // 서치 페이징 기준 확인
        if(size!=null && List.of(10, 30, 50).contains(size)){
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(hubService.readAllHub(hubId, pageable));
    }

    @Operation(summary="허브 검색", description="특정 허브를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<HubResponseDto> searchHub(@RequestParam String hubName) {

        return ResponseEntity.ok(hubService.searchHub(hubName));
    }

    // 권한 -> MASTER
    @Operation(summary="허브 삭제", description="특정 허브를 삭제합니다.")
    @DeleteMapping("/{hubId}")
    @PreAuthorize("hasRole('Master')")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return ResponseEntity.noContent().build();
    }

    // 업체 생성 및 수정될 때 허브 id가 실제로 존재하는 허브인지 확인
    // 존재하면 true, 안하면 false
    @GetMapping("/check/{hubId}")
    public boolean isHubIdExist(@PathVariable UUID hubId) {
        return hubService.isHubIdExist(hubId); // true / false
    }



}
