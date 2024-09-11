package com.team11.delivery_service.domain.model;

public enum DeliveryStatusEnum {
    PENDING("허브 대기 중"),
    MOVING("허브 간 이동 중"),
    ARRIVED("목적지 허브 도착"),
    SHIPPING("배송 중"),
    DELIVERED("배송 완료");

    private final String status;

    DeliveryStatusEnum(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
