package com.usertest.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.basedto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

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

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(ResourceAccessException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(JsonProcessingException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(IncorrectResponseEntityStatus exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    ResponseEntity<ResponseDto<String>> handleException(IncorrectStatusDto exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    ResponseEntity<ResponseDto<String>> handleException(RestClientException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.EXPECTATION_FAILED);
    }
}
