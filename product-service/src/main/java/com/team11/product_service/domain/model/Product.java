package com.team11.product_service.domain.model;

import com.team11.product_service.presentation.request.ProductReqDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_products")
public class Product extends BaseEntity {
    @Id
    @Column(name="product_id")
    private UUID productId = UUID.randomUUID();

    @Column(name="product_name")
    private String productName;

    @Column(name="company_id")
    private UUID companyId;

    @Column(name="hub_id")
    private UUID hubId;

    @Column(name="is_Deleted")
    private boolean deleted = false;

    @Column(name="stock")
    private int stock;

}
