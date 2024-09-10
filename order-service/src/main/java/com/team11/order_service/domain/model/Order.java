package com.team11.order_service.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_orders")
public class Order extends BaseEntity {
    @Id
    @Column(name="order_id")
    private UUID orderId;

    @Column(name="delivery_id")
    private UUID deliveryId;

    @Column(name="product_id")
    private UUID productId;

    @Column(name="quantity")
    private int quantity;

    @Column(name="supply_company_id")
    private UUID supplyCompanyId;

    @Column(name="receive_company_id")
    private UUID receiveCompanyId;

    @Column(name="is_deleted")
    private boolean deleted;

}
