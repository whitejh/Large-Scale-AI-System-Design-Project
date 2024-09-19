package com.team11.company_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_companies")
public class Company extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="company_id")
    private UUID companyId;

    @Column(name="company_name")
    private String companyName;

    @Column(name="company_type")
    @Enumerated(value=EnumType.STRING)
    private CompanyTypeEnum type;

    @Column(name="hub_id")
    private UUID hubId;

    @Column(name="company_address")
    private String companyAddress;

}
