package com.team11.delivery_service.presentation.controller;

import com.team11.delivery_service.application.dto.DeliveryRespDto;
import com.team11.delivery_service.application.service.DeliveryService;
import com.team11.delivery_service.presentation.request.RecipientReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // 배송 조회
}
