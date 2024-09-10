package com.team11.order_service.application.service;

import com.team11.order_service.application.dto.OrderRespDto;
import com.team11.order_service.domain.model.Order;
import com.team11.order_service.domain.repository.OrderRepository;
import com.team11.order_service.infrastructure.feign.ProductFeignClient;
import com.team11.order_service.presentation.request.OrderReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private ProductFeignClient productFeignClient;

    public OrderRespDto createOrder(OrderReqDto reqDto) {
        Order order = OrderReqDto.toOrder(reqDto);

        int quantity = order.getQuantity();
        int stock = productFeignClient.getStockByProductId(order.getProductId());

        // 재고 확인
        if(stock >= quantity) {
            log.info("상품의 재고가 주문 수량보다 많음. 주문 생성 성공");
            productFeignClient.updateStockByProductId(order.getProductId(), quantity);
        }else {
            log.warn("상품의 재고가 주문 수량보다 부족함. 주문 생성 실패.");
            throw new IllegalArgumentException("재고가 부족합니다.");

        }

        return OrderRespDto.from(order);
    }
}
