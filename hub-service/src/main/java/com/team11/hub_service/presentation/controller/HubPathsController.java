package com.team11.hub_service.presentation.controller;


import com.team11.hub_service.application.dto.HubPathsRespDto;
import com.team11.hub_service.application.dto.HubResponseDto;
import com.team11.hub_service.application.service.HubPathsService;
import com.team11.hub_service.domain.model.HubPaths;
import com.team11.hub_service.presentation.request.HubPathsReqDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
//import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="HubPaths", description="HubPaths API")
@RestController
@RequestMapping("/hubPaths")
@Slf4j
@RequiredArgsConstructor
public class HubPathsController {

    private final HubPathsService hubPathsService;

    @Operation(summary="허브 이동경로 추가", description="허브를 이동 경로를 추가합니다.")
    @PostMapping
    //@PreAuthorize("hasRole('Master')")
    public ResponseEntity<HubPathsRespDto> createHubPaths (@RequestBody @Valid HubPathsReqDto reqDto) {
        return ResponseEntity.ok(hubPathsService.createHubPaths(reqDto));
    }

    @Operation(summary="허브 이동경로 수정", description="허브의 이동 경로를 수정합니다.")
    @PutMapping("/{hubPathsId}")
    //@PreAuthorize("hasRole('Master')")
    public ResponseEntity<HubPathsRespDto> updateHubPaths(@RequestBody HubPathsReqDto reqDto, @PathVariable UUID hubPathsId){
        return ResponseEntity.ok(hubPathsService.updateHubPaths(reqDto, hubPathsId));
    }

    @Operation(summary="허브 이동경로 검색", description="특정 허브를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<HubPathsRespDto> searchHubPaths(@RequestParam String path) {

        return ResponseEntity.ok(hubPathsService.searchHubPaths(path));
    }


    @Operation(summary="허브 이동경로 단건 조회", description="허브 이동경로를 단건 조회합니다.")
    @GetMapping("/{hubPathsId}")
    public ResponseEntity<HubPathsRespDto> findHubPaths(@PathVariable UUID hubPathsId){
        return ResponseEntity.ok(hubPathsService.findHubPaths(hubPathsId));
    }

    @Operation(summary="허브 이동경로 전체 조회", description="허브 이동경로를 전체 조회합니다.")
    @GetMapping
    public ResponseEntity<List<HubPathsRespDto>> readHubPaths(@PathVariable UUID hubPathsId,
                                                        @PageableDefault(size=10) Pageable pageable,
                                                        @RequestParam(name="size", required = false) Integer size){

        // 서치 페이징 기준 확인
        if(size!=null && List.of(10, 30, 50).contains(size)){
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(hubPathsService.findAllHubPaths(hubPathsId, pageable));
    }

    @Operation(summary="허브 이동경로 삭제", description="허브 이동경로를 삭제합니다.")
    @DeleteMapping("/{hubPathsId}")
    //@PreAuthorize("hasRole('Master')")
    public ResponseEntity<Void> deleteHubPaths(@PathVariable UUID hubPathsId) {
        hubPathsService.deleteHubPaths(hubPathsId);
        return ResponseEntity.noContent().build();
    }
}
