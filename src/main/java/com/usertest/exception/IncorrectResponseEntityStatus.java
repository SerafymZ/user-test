package com.usertest.exception;

public class IncorrectResponseEntityStatus extends RuntimeException{

    public IncorrectResponseEntityStatus(String message) {
        super(message);
    }
}
