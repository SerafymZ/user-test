package com.usertest.service.responsevalidationservice;

import com.usertest.dto.basedto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface ResponseValidationService {
    void throwIfResponseEntityNotValid(ResponseEntity responseEntity, AddressOperationType operationType);

    void throwIfStatusResponseDtoNotValid(ResponseDto dto, AddressOperationType operationType);
}
