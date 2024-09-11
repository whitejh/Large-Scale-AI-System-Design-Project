package com.team11.order_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="order_id")
    private UUID orderId;

    @Column(name="username")
    private String userName;

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

}
