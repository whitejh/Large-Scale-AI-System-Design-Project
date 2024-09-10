package com.team11.order_service.presentation.controller;

import com.team11.order_service.application.dto.OrderRespDto;
import com.team11.order_service.application.service.OrderService;
import com.team11.order_service.presentation.request.OrderReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Order", description="Order API")
@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary="주문 생성", description="새 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderRespDto> createOrder(OrderReqDto orderReqDto) {
        return ResponseEntity.ok(orderService.createOrder(orderReqDto));
    }
}
