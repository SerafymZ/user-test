package com.usertest.service.responsevalidationservice;

public enum AddressOperationType {
    FIND_OR_INSERT_ADDRESS,
    GET_ADDRESS_BY_ID,
    UPDATE_ADDRESS,
    DELETE_ADDRESS_BY_ID;

    public String getDisplayName() {
        if (this == FIND_OR_INSERT_ADDRESS) return "find or insert address";
        else if (this == GET_ADDRESS_BY_ID) return "get address by id";
        else if (this == UPDATE_ADDRESS) return  "update address";
        else if (this == DELETE_ADDRESS_BY_ID) return "delete address by id";
        else return "unknown operation";
    }
}
