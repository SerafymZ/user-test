package com.usertest.service.responsevalidationservice;

import com.usertest.dto.basedto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ResponseValidationService {
    void checkStatusResponseEntity(ResponseEntity responseEntity, AddressOperationType operationType);

    void checkStatusResponseDto(ResponseDto dto, AddressOperationType operationType);
}
