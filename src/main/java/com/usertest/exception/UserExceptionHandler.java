package com.usertest.exception;

import com.usertest.dto.basedto.ErrorDto;
import com.usertest.dto.basedto.ResponseDto;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(NotFoundUserException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(IncorrectResponseEntityStatus exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(DataAccessException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(MappingException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<String>> handleException(ResourceAccessException exception) {
        return new ResponseEntity<>(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Object>> handleException(MethodArgumentNotValidException exception) {
        List<ErrorDto> errors = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            var error = new ErrorDto();
            error.setMessage("Field '" + fieldError.getField() + "' : " + fieldError.getDefaultMessage());
            errors.add(error);
        }
        return new ResponseEntity<>(ResponseDto.failedResponseDto(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Object>> handleException(ConstraintViolationException exception) {
        var error = new ErrorDto();
        error.setMessage(exception.getMessage());
        return new ResponseEntity<>(ResponseDto.failedResponseDto(List.of(error)), HttpStatus.BAD_REQUEST);
    }
}
