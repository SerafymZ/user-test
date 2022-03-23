package com.usertest.exception;

import com.usertest.dto.basedto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(NotFoundUserException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(NotFoundNumberException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
