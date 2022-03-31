package com.usertest.service.responsevalidationservice;

import lombok.Getter;

@Getter
public enum AddressOperationType {
    FIND_OR_INSERT_ADDRESS("find or insert address"),
    GET_ADDRESS_BY_ID("get address by id"),
    UPDATE_ADDRESS("update address"),
    DELETE_ADDRESS_BY_ID("delete address by id");

    private final String displayName;

    AddressOperationType(String displayName) {
        this.displayName = displayName;
    }
}
