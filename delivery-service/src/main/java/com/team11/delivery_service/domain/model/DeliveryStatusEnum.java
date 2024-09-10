package com.team11.delivery_service.domain.model;

public enum DeliveryStatusEnum {
    PENDING(Status.PENDING),
    MOVING(Status.MOVING),
    ARRIVED(Status.ARRIVED),
    SHIPPING(Status.SHIPPING),
    DELIVERED(Status.DELIVERED);

    private final String status;

    DeliveryStatusEnum(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public static class Status {
        public static final String PENDING = "허브 대기 중";
        public static final String MOVING = "허브 간 이동 중";
        public static final String ARRIVED = "목적지 허브 도착";
        public static final String SHIPPING = "배송 중";
        public static final String DELIVERED = "배송 완료";
    }
}
