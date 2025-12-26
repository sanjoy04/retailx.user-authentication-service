package com.retailx.user_authentication_service.model;

public enum Role {
    CUSTOMER("CUSTOMER"),
    SELLER("SELLER"),
    ADMIN("ADMIN");
    private String value;
    Role(String value) {}
}
