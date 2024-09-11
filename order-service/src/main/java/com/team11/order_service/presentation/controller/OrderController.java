package com.team11.order_service.presentation.controller;

import com.team11.order_service.application.dto.OrderRespDto;
import com.team11.order_service.application.service.OrderService;
import com.team11.order_service.presentation.request.OrderReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name="Order", description="Order API")
@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary="주문 생성", description="새 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderRespDto> createOrder(OrderReqDto orderReqDto, @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(orderService.createOrder(orderReqDto, userName));
    }

    @Operation(summary="주문 수정", description="주문을 수정합니다.")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderRespDto> updateOrder(@PathVariable UUID orderId, OrderReqDto orderReqDto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderReqDto));
    }

    @Operation(summary="주문 삭제", description="주문을 삭제합니다.")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderRespDto> deleteOrder(@PathVariable UUID orderId, @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId, userName));
    }

    @Operation(summary="주문 상세 조회", description="특정 주문을 상세 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRespDto> getOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    @Operation(summary="주문 전체 조회(업체 별)", description="해당 업체의 주문들을 전체 조회합니다.")
    @GetMapping("/{companyId}")
    public ResponseEntity<List<OrderRespDto>> getOrdersOfCompany(@PathVariable UUID companyId) {
        return ResponseEntity.ok(orderService.getOrdersOfCompany(companyId));
    }




}
