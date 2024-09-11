package com.team11.delivery_service.presentation.controller;

import com.team11.delivery_service.application.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 배송 삭제

    // 배송 조회
}
