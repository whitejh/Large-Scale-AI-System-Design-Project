package com.team11.order_service.presentation.request;

import com.team11.order_service.domain.model.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDto {
    private UUID orderId;
    private String userName;
    private UUID deliveryId;

    @NotBlank
    private UUID productId;
    @NotBlank(message="상품의 개수를 꼭 입력해주세요.")
    private int quantity;

    @NotBlank
    private UUID supplyCompany;
    @NotBlank
    private UUID receiveCompany;

    @NotBlank
    private String recipientName;
    @NotBlank
    private UUID recipientSlackId;

    public static Order toOrder(OrderReqDto reqDtoDto, String userName) {
        return Order.builder()
                .userName(userName)
                .productId(reqDtoDto.getProductId())
                .quantity(reqDtoDto.getQuantity())
                .supplyCompanyId(reqDtoDto.getSupplyCompany())
                .receiveCompanyId(reqDtoDto.getReceiveCompany())
                .build();
    }
}
