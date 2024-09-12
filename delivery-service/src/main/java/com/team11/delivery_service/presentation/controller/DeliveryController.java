package com.team11.delivery_service.presentation.controller;

import com.team11.delivery_service.application.dto.DeliveryRespDto;
import com.team11.delivery_service.application.service.DeliveryService;
import com.team11.delivery_service.presentation.request.RecipientReqDto;
import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    // 배송 생성
    @Operation(summary="배송 생성", description="새 배송을 생성합니다.")
    @PostMapping
    public UUID createDelivery(UUID supplyCompanyId, UUID receiveCompanyId, String recipientName, UUID recipientSlackId){
        return deliveryService.createDelivery(supplyCompanyId, receiveCompanyId, recipientName, recipientSlackId);
    }

    // 배송 수정
    @Operation(summary="배송 수정", description="배송 정보를 수정합니다.")
    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryRespDto> updateDelivery(@Validated @RequestBody RecipientReqDto reqDto, @PathVariable(name="deliveryId") UUID deliveryId) {
        return ResponseEntity.ok(deliveryService.updateDelivery(reqDto, deliveryId));
    }

    // 배송 삭제
    @Operation(summary="배송 삭제", description="배송 정보를 삭제합니다.")
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<DeliveryRespDto> deleteDelivery(@PathVariable(name="deliveryId") UUID deliveryId, @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(deliveryService.deleteDelivery(deliveryId, userName));
    }

    // 배송 조회 (허브 별)
    @Operation(summary = "배송 조회(허브 별)", description = "허브 별 배송 내역을 조회합니다.")
    @GetMapping("/searchByHub/{hubId}")
    public ResponseEntity<List<DeliveryRespDto>> getDeliveryByHub(@PathVariable(name="hubId") UUID hubId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByHub(hubId));
    }

    // 배송 조회 (업체 별)
    @Operation(summary="배송 조회(업체 별)", description = "업체 별 배송 내역을 조회합니다.")
    @GetMapping("/searchByCompany/{companyId}")
    public ResponseEntity<List<DeliveryRespDto>> getDeliveryByCompany(@PathVariable(name="companyId") UUID companyId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByCompany(companyId));
    }

    // 배송 조회 (배송 담당자 별)
}
