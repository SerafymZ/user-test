package com.usertest.service.responsevalidationservice;

import com.usertest.dto.basedto.ResponseDto;
import com.usertest.dto.basedto.Status;
import com.usertest.exception.IncorrectResponseEntityStatus;
import com.usertest.exception.IncorrectStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseValidationServiceImpl implements ResponseValidationService {
    @Override
    public void throwIfResponseEntityNotValid(ResponseEntity responseEntity, AddressOperationType operationType) {
        int statusCode = responseEntity.getStatusCodeValue();
        boolean isStatusCodeCorrect = statusCode >= HttpStatus.OK.value()
                && statusCode <HttpStatus.MULTIPLE_CHOICES.value();
        if (!isStatusCodeCorrect) {
            throw new IncorrectResponseEntityStatus("Incorrect response entity status on operation " +
                    operationType.getDisplayName() + ". Received status: " + responseEntity.getStatusCode().name());
        }
    }

    @Override
    public void throwIfStatusResponseDtoNotValid(ResponseDto dto, AddressOperationType operationType) {
        Status status = dto.getStatus();
        if (status != Status.OK) {
            throw new IncorrectStatusDto("Incorrect response dto status on operation "
                    + operationType.getDisplayName() + ". Received status: " + status.name());
        }
    }
}
