package com.team11.delivery_service.presentation.controller;

import com.team11.delivery_service.application.dto.DeliveryRespDto;
import com.team11.delivery_service.application.service.DeliveryService;
import com.team11.delivery_service.presentation.request.RecipientReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Delivery", description="Delivery API")
@RestController
@RequestMapping("/deliveries")
@Slf4j
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 주문 생성 시 자동으로 생성
    @Operation(summary="배송 생성", description="새 배송을 생성합니다.")
    @PostMapping
    public UUID createDelivery(UUID supplyCompanyId,
                               UUID receiveCompanyId,
                               String recipientName,
                               UUID recipientSlackId)
    {
        return deliveryService.createDelivery(supplyCompanyId, receiveCompanyId, recipientName, recipientSlackId);
    }

    // 권한 -> MASTER, MANAGER(본인 허브 소속만), DRIVER(본인 배송 담당만)
    @Operation(summary="배송 수정", description="배송 정보를 수정합니다.")
    @PutMapping("/{deliveryId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('DRIVER')")
    public ResponseEntity<DeliveryRespDto> updateDelivery(@Validated @RequestBody RecipientReqDto reqDto,
                                                          @PathVariable(name="deliveryId") UUID deliveryId) {
        return ResponseEntity.ok(deliveryService.updateDelivery(reqDto, deliveryId));
    }

    // 배송 삭제
    @Operation(summary="배송 삭제", description="배송 정보를 삭제합니다.")
    @DeleteMapping("/{deliveryId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<DeliveryRespDto> deleteDelivery(@PathVariable(name="deliveryId") UUID deliveryId,
                                                          @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(deliveryService.deleteDelivery(deliveryId, userName));
    }

    // 권한 -> MASTER, MANAGER
    @Operation(summary = "배송 조회(허브 별)", description = "허브 별 배송 내역을 조회합니다.")
    @GetMapping("/searchByHub/{hubId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER')")
    public ResponseEntity<List<DeliveryRespDto>> getDeliveryByHub(@PathVariable(name="hubId") UUID hubId,
                                                                  @PageableDefault(size=10) Pageable pageable,
                                                                  @RequestParam(name="size", required = false) Integer size)
    {
        // 서치 페이징 기준 확인
        if (size != null && List.of(10, 30, 50).contains(size)) {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(deliveryService.getDeliveryByHub(hubId, pageable));
    }

    // 권한 -> MASTER, COMPANY
    @Operation(summary="배송 조회(업체 별)", description = "업체 별 배송 내역을 조회합니다.")
    @GetMapping("/searchByCompany/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('COMPANY')")
    public ResponseEntity<List<DeliveryRespDto>> getDeliveryByCompany(@PathVariable(name="companyId") UUID companyId,
                                                                      @PageableDefault(size=10) Pageable pageable,
                                                                      @RequestParam(name="size", required = false) Integer size
    ) {
        // 서치 페이징 기준 확인
        if (size != null && List.of(10, 30, 50).contains(size)) {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(deliveryService.getDeliveryByCompany(companyId, pageable));
    }

    // 권한 -> MASTER, DRIVER(본인 배송 담당만)
    // 배송 조회 (배송 담당자 별)
}
