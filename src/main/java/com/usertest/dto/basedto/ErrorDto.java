package com.usertest.dto.basedto;

import lombok.Data;

@Data
public class ErrorDto {
    private int code;
    private int source;
    private String message;
}
