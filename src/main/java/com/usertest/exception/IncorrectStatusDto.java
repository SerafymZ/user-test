package com.usertest.exception;

public class IncorrectStatusDto extends RuntimeException{

    public IncorrectStatusDto(String message) {
        super(message);
    }
}
