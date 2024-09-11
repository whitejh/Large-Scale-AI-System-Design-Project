package com.team11.order_service.application.service;

import com.team11.order_service.application.dto.OrderRespDto;
import com.team11.order_service.domain.model.Order;
import com.team11.order_service.domain.repository.OrderRepository;
import com.team11.order_service.infrastructure.feign.ProductFeignClient;
import com.team11.order_service.presentation.request.OrderReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private ProductFeignClient productFeignClient;

    // 주문 생성
    public OrderRespDto createOrder(OrderReqDto reqDto, String userName) {
        Order order = OrderReqDto.toOrder(reqDto, userName);

        int quantity = order.getQuantity();
        int stock = productFeignClient.getStockByProductId(order.getProductId());

        // 재고 확인
        if(stock >= quantity) {
            log.info("상품의 재고가 주문 수량보다 많음. 주문 생성 성공");
            productFeignClient.updateStockByProductId(order.getProductId(), stock-quantity);
        }else {
            log.warn("상품의 재고가 주문 수량보다 부족함. 주문 생성 실패.");
            throw new IllegalArgumentException("재고가 부족합니다.");

        }

        orderRepository.save(order);

        return OrderRespDto.from(order);
    }

    // 주문 수정
    public OrderRespDto updateOrder(UUID orderId, OrderReqDto reqDto) {
        Order order = orderRepository.findByOrderIdAndDeletedIsFalse(orderId).orElseThrow(
                ()-> new IllegalArgumentException("수정하려는 주문을 찾을 수 없습니다.")
        );

        int originStock = order.getQuantity() + productFeignClient.getStockByProductId(order.getProductId());
        int newQuantity = reqDto.getQuantity();

        // 재고 확인
        if(originStock >= newQuantity) {
            log.info("상품의 재고가 주문 수량보다 많음. 주문 생성 성공");
            productFeignClient.updateStockByProductId(order.getProductId(),originStock-newQuantity);
        }else {
            log.warn("상품의 재고가 주문 수량보다 부족함. 주문 생성 실패.");
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        orderRepository.save(order);

        return OrderRespDto.from(order);
    }

    // 주문 삭제
    public OrderRespDto deleteOrder(UUID orderId, String userName) {
        Order order = orderRepository.findByOrderIdAndDeletedIsFalse(orderId).orElseThrow(
                ()-> new IllegalArgumentException("삭제하려는 주문이 존재하지 않습니다.")
        );

        int stock = productFeignClient.getStockByProductId(order.getProductId());

        productFeignClient.updateStockByProductId(orderId, order.getQuantity() + stock);

        order.setDeleted(LocalDateTime.now(), userName);

        orderRepository.save(order);

        return OrderRespDto.from(order);
    }

    // 주문 상세 조회
    public OrderRespDto getOrderDetails(UUID orderId) {
        Order order = orderRepository.findByOrderIdAndDeletedIsFalse(orderId).orElseThrow(
                ()-> new IllegalArgumentException("조회하려는 주문이 존재하지 않습니다.")
        );

        return OrderRespDto.from(order);
    }

    // 주문 전체 조회
    public List<OrderRespDto> getOrders(String userName) {
        List<Order> orderList = orderRepository.findAllByUserNameAndDeletedIsFalse(userName).orElseThrow(
                ()-> new IllegalArgumentException("해당 사용자의 주문 내역이 없습니다.")
        );

        return orderList.stream().map(OrderRespDto::from).collect(Collectors.toList());
    }
}
