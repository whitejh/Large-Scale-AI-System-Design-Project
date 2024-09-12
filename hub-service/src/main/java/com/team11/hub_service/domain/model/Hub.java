package com.team11.hub_service.domain.model;

import com.team11.hub_service.presentation.request.HubRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="p_hubs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access= AccessLevel.PUBLIC)
public class Hub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", updatable = false, nullable = false)
    private UUID hubId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "order_id", nullable = false)
//    private Order order; // FK

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name="is_deleted")
    private boolean delete = false;


//    public static Hub createHub(HubRequestDto requestDto) {
//        return Hub.builder()
//                .name(requestDto.getName())
//                .address(requestDto.getAddress())
//                .latitude(requestDto.getLatitude())
//                .longitude(requestDto.getLongitude())
//                .build();
//    }

    public void updateHub(HubRequestDto requestDto) {
        this.name = requestDto.getName();
        this.address = requestDto.getAddress();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
    }




}
