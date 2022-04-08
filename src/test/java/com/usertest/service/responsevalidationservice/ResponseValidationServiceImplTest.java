package com.usertest.service.responsevalidationservice;

import com.usertest.dto.basedto.ResponseDto;
import com.usertest.exception.IncorrectResponseEntityStatus;
import com.usertest.exception.IncorrectStatusDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = ResponseValidationServiceImpl.class)
class ResponseValidationServiceImplTest {

    @Autowired
    ResponseValidationServiceImpl responseValidationService;

    @Test
    void throwIfResponseEntityNotValid_shouldBeThrewIncorrectResponseEntityStatus() {
        //given
        var operationType = AddressOperationType.GET_ADDRESS_BY_ID;
        var responseEntity = new ResponseEntity<>("BODY", HttpStatus.BAD_REQUEST);

        //when
        assertThatThrownBy(() -> responseValidationService.throwIfResponseEntityNotValid(responseEntity, operationType))
                .isInstanceOf(IncorrectResponseEntityStatus.class);
    }

    @Test
    void throwIfResponseEntityNotValid_shouldBeDoNothing() {
        //given
        var operationType = AddressOperationType.GET_ADDRESS_BY_ID;
        var responseEntity = new ResponseEntity<>("BODY", HttpStatus.OK);

        responseValidationService.throwIfResponseEntityNotValid(responseEntity, operationType);
    }

    @Test
    void throwIfStatusResponseDtoNotValid_shouldBeThrewIncorrectStatusDto() {
        //given
        var operationType = AddressOperationType.GET_ADDRESS_BY_ID;
        var responseDto = ResponseDto.failedResponseDto("BODY");

        //when
        assertThatThrownBy(() -> responseValidationService.throwIfStatusResponseDtoNotValid(responseDto, operationType))
                .isInstanceOf(IncorrectStatusDto.class);
    }

    @Test
    void throwIfStatusResponseDtoNotValid_shouldBeDoNothing() {
        //given
        var operationType = AddressOperationType.GET_ADDRESS_BY_ID;
        var responseDto = ResponseDto.okResponseDto("BODY");

        //when
        responseValidationService.throwIfStatusResponseDtoNotValid(responseDto, operationType);
    }
}