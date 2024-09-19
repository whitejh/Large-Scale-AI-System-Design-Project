package com.team11.order_service.presentation.controller;

import com.team11.order_service.application.dto.OrderRespDto;
import com.team11.order_service.application.service.OrderService;
import com.team11.order_service.presentation.request.OrderReqDto;
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

@Tag(name="Order", description="Order API")
@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER
    @Operation(summary="주문 생성", description="새 주문을 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<OrderRespDto> createOrder( @RequestBody OrderReqDto orderReqDto,
                                                    @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(orderService.createOrder(orderReqDto, userName));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER - (해당 주문자만 가능)
    @Operation(summary="주문 수정 - 재고 변경", description="주문을 수정합니다.")
    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<OrderRespDto> updateOrder(
                                                    @PathVariable(name="orderId") UUID orderId,
                                                    @RequestParam(name="newQuantity") int newQuantity,
                                                    @RequestHeader(name="X-User-Name") String userName,
                                                    @RequestHeader(name="X-User-Roles") String role)
    {
        return ResponseEntity.ok(orderService.updateOrder(orderId, newQuantity, userName, role));
    }

    @Operation(summary="주문 삭제", description="주문을 삭제합니다.")
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<OrderRespDto> deleteOrder(
                                                    @PathVariable(name="orderId") UUID orderId,
                                                    @RequestHeader(name="X-User-Name") String userName) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId, userName));
    }

    // 권한 -> MASTER, MANAGER, COMPANY, DRIVER - (본인 주문만)
    @Operation(summary="주문 상세 조회", description="특정 주문을 상세 조회합니다.")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('COMPANY') or hasRole('DRIVER')")
    public ResponseEntity<OrderRespDto> getOrderDetails(@PathVariable(name="orderId") UUID orderId,
                                                        @RequestHeader(name="X-User-Name") String userName,
                                                        @RequestHeader(name="X-Usre-Roles") String role){
        return ResponseEntity.ok(orderService.getOrderDetails(orderId, userName, role));
    }

    // 권한 -> MASTER, COMPANY(본인 업체 소속만)
    @Operation(summary="주문 전체 조회(업체)", description="해당 업체의 주문들을 전체 조회합니다.")
    @GetMapping("/search/{companyId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('COMPANY')")
    public ResponseEntity<List<OrderRespDto>> getOrdersOfCompany(
                                                                @PathVariable(name="companyId") UUID companyId,
                                                                @RequestHeader(name="X-User-Name") String userName,
                                                                @RequestHeader(name="X-Usre-Roles") String role,
                                                                @PageableDefault(size=10) Pageable pageable,
                                                                @RequestParam(name="size", required = false) Integer size)
    {
        // 서치 페이징 기준 확인
        if (size != null && List.of(10, 30, 50).contains(size)) {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        return ResponseEntity.ok(orderService.getOrdersOfCompany(companyId, pageable, userName, role));
    }

}
