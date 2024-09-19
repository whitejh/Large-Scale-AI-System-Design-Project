package com.team11.company_service.domain.model;

public enum CompanyTypeEnum {
    VENDOR(Type.VENDOR),
    CLIENT(Type.CLIENT);

    private final String type;

    CompanyTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static class Type {
        public static final String VENDOR = "생산 업체";
        public static final String CLIENT = "수령 업체";
    }
}
