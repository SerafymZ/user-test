package com.usertest.exception;

import com.usertest.dto.basedto.ResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(NotFoundUserException exception) {
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
    ResponseEntity<ResponseDto<String>> handleException(MaxStringFieldValueExceedException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(DataAccessException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(NullPointerException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(IncorrectAgeException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(MappingException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(ResourceAccessException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
