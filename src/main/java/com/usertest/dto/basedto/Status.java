package com.usertest.dto.basedto;

public enum Status {
    OK(1),
    Failed(2),
    Forbidden(3);

    public Integer status;

    private Status(Integer status) {
        this.status = status;
    }
}
