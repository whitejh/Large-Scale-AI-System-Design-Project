package com.team11.deliveryDriver_service.presentation.controller;

import com.team11.deliveryDriver_service.application.dto.DriverRespDto;
import com.team11.deliveryDriver_service.application.service.DriverService;
import com.team11.deliveryDriver_service.presentation.request.DriverReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Delivery-Drivers", description="Delivery-Drivers API")
@RestController
@RequestMapping("/drivers")
@Slf4j
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @Operation(summary="배송 담당자 추가", description="새 배송 담당자를 추가합니다.")
    @PostMapping
    public ResponseEntity<DriverRespDto> createDriver(@Validated @RequestBody DriverReqDto reqDto) {
        return ResponseEntity.ok(driverService.createDriver(reqDto));
    }

    @Operation(summary="배송 담당자 정보 수정", description="배송 담당자 정보를 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<DriverRespDto> updateDriver(@Validated @RequestBody DriverReqDto reqDto, @PathVariable Long userId) {
        return ResponseEntity.ok(driverService.updateDriver(reqDto, userId));
    }

    @Operation(summary="배송 담당자 삭제", description="배송 담당자를 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<DriverRespDto> deleteDriver(@PathVariable Long userId, @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(driverService.deleteDriver(userId, userName));
    }

    @Operation(summary="배송 담당자 조회(개인)", description="배송 담당자 본인의 정보를 조회합니다.")
    @GetMapping("/search/{userId}")
    public ResponseEntity<DriverRespDto> getDriver(@PathVariable Long userId) {
        return ResponseEntity.ok(driverService.getDriver(userId));
    }

    @Operation(summary="배송 담당자 조회(허브)", description="특정 허브에 소속된 배송 담당자들의 정보를 조회합니다.")
    @GetMapping("/searchAll/{hubId}")
    public ResponseEntity<List<DriverRespDto>> getDrivers(@PathVariable UUID hubId,
                                                          @PageableDefault(size=10) Pageable pageable,
                                                          @RequestParam(name="size", required = false) Integer size
    ) {
        // 서치 페이징 기준 확인
        if (size != null && List.of(10, 30, 50).contains(size)) {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(driverService.getAllDrivers(hubId, pageable));
    }


    // FeignClient

    @Operation(summary="슬랙 ID 업데이트", description="사용자가 변경한 slack_id 정보를 갱신합니다.")
    @PutMapping("/slackIdUpdate/{userId}/{slackId}")
    public void updateSlackId(@PathVariable(name="userId") Long userId, @PathVariable(name="slackId") UUID slackId) {
        driverService.updateSlackId(userId, slackId);
    }
}
