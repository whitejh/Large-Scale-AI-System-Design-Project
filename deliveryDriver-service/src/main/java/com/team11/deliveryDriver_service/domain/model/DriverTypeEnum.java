package com.team11.deliveryDriver_service.domain.model;

public enum DriverTypeEnum {
    HUB_DRIVER("허브 배송 담당자"),
    COMPANY_DRIVER("업체 배송 담당자");

    private final String driver;

    DriverTypeEnum(String driver){
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
