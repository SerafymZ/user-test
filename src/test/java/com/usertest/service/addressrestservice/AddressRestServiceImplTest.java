package com.usertest.service.addressrestservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.exception.IncorrectResponseEntityStatus;
import com.usertest.exception.IncorrectStatusDto;
import com.usertest.service.mappingservice.MappingService;
import com.usertest.service.responsevalidationservice.AddressOperationType;
import com.usertest.service.responsevalidationservice.ResponseValidationServiceImpl;
import com.usertest.service.restservice.RestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AddressRestServiceImpl.class)
class AddressRestServiceImplTest {

    private static final String ADDRESS = "Canada";
    private static final String ERROR_BODY = "Error";
    private static final String BODY = "body";
    private static final Long ADDRESS_ID = 1L;

    @Value("${address-service.url}")
    private String addressUrl;

    @Autowired
    AddressRestServiceImpl addressRestService;

    @MockBean
    RestService restService;

    @MockBean
    MappingService mappingService;

    @MockBean
    ResponseValidationServiceImpl responseValidationService;

    @Test
    void findOrInsertAddress_shouldBeThrewIncorrectResponseEntityStatus() {
        //given
        var addressDto = createAddressDto();
        var httpEntity = new HttpEntity<>(addressDto);
        var responseEntity = new ResponseEntity<>(ERROR_BODY, HttpStatus.BAD_REQUEST);
        when(restService.sendPost(addressUrl, httpEntity)).thenReturn(responseEntity);
        doThrow(IncorrectResponseEntityStatus.class).when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);

        //when
        assertThatThrownBy(() -> addressRestService.findOrInsertAddress(addressDto))
                .isInstanceOf(IncorrectResponseEntityStatus.class);

        //then
        verify(restService, times(1)).sendPost(addressUrl, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
    }

    @Test
    void findOrInsertAddress_shouldBeThrew() {
        //given
        var addressDto = createAddressDto();
        var httpEntity = new HttpEntity<>(addressDto);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.BAD_REQUEST);
        when(restService.sendPost(addressUrl, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        var resultAddressDto = createAddressDto();
        resultAddressDto.setId(1L);
        var responseDto = ResponseDto.okResponseDto(resultAddressDto);
        when(mappingService.readAddressDto(responseEntity.getBody())).thenReturn(responseDto);
        doThrow(IncorrectStatusDto.class).when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.FIND_OR_INSERT_ADDRESS);

        //when
        assertThatThrownBy(() -> addressRestService.findOrInsertAddress(addressDto))
                .isInstanceOf(IncorrectStatusDto.class);

        //then
        verify(restService, times(1)).sendPost(addressUrl, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        verify(mappingService, times(1)).readAddressDto(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.FIND_OR_INSERT_ADDRESS);
    }

    @Test
    void findOrInsertAddress_shouldBeSendPostRequestSuccessful() {
        //given
        var addressDto = createAddressDto();
        var httpEntity = new HttpEntity<>(addressDto);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendPost(addressUrl, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        var resultAddressDto = createAddressDto();
        resultAddressDto.setId(1L);
        var responseDto = ResponseDto.okResponseDto(resultAddressDto);
        when(mappingService.readAddressDto(responseEntity.getBody())).thenReturn(responseDto);
        doNothing().when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.FIND_OR_INSERT_ADDRESS);

        //when
        var actualResult = addressRestService.findOrInsertAddress(addressDto);

        //then
        assertThat(actualResult).isEqualTo(resultAddressDto);
        verify(restService, times(1)).sendPost(addressUrl, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        verify(mappingService, times(1)).readAddressDto(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.FIND_OR_INSERT_ADDRESS);
    }

    @Test
    void getAddressById_shouldBeThrewIncorrectResponseEntityStatus() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(ERROR_BODY, HttpStatus.OK);
        when(restService.sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doThrow(IncorrectResponseEntityStatus.class).when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);

        //when
        assertThatThrownBy(() -> addressRestService.getAddressById(ADDRESS_ID))
                .isInstanceOf(IncorrectResponseEntityStatus.class);

        verify(restService, times(1)).sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);
    }

    @Test
    void getAddressById_shouldBeThrewIncorrectStatusDto() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);

        var resultAddressDto = createAddressDto();
        resultAddressDto.setId(ADDRESS_ID);
        var responseAddressDto = ResponseDto.okResponseDto(resultAddressDto);
        when(mappingService.readAddressDto(responseEntity.getBody())).thenReturn(responseAddressDto);
        doThrow(IncorrectStatusDto.class).when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseAddressDto, AddressOperationType.GET_ADDRESS_BY_ID);

        //when
        assertThatThrownBy(() -> addressRestService.getAddressById(ADDRESS_ID));

        //then
        verify(restService, times(1)).sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);
        verify(mappingService, times(1)).readAddressDto(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseAddressDto, AddressOperationType.GET_ADDRESS_BY_ID);
    }

    @Test
    void getAddressById_shouldBeFindAddressSuccessful() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);

        var resultAddressDto = createAddressDto();
        resultAddressDto.setId(ADDRESS_ID);
        var responseAddressDto = ResponseDto.okResponseDto(resultAddressDto);
        when(mappingService.readAddressDto(responseEntity.getBody())).thenReturn(responseAddressDto);
        doNothing().when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseAddressDto, AddressOperationType.GET_ADDRESS_BY_ID);

        //when
        var actualResult = addressRestService.getAddressById(ADDRESS_ID);

        //then
        assertThat(actualResult).isEqualTo(resultAddressDto);

        verify(restService, times(1)).sendGet(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);
        verify(mappingService, times(1)).readAddressDto(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseAddressDto, AddressOperationType.GET_ADDRESS_BY_ID);
    }

    @Test
    void deleteAddressById_shouldByThrewIncorrectResponseEntityStatus() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doThrow(IncorrectResponseEntityStatus.class).when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);

        //when
        assertThatThrownBy(() -> addressRestService.deleteAddressById(ADDRESS_ID))
                .isInstanceOf(IncorrectResponseEntityStatus.class);

        verify(restService, times(1)).sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
    }

    @Test
    void deleteAddressById_shouldByThrewIncorrectStatusDto() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        var result = 1;
        var responseDto = ResponseDto.okResponseDto(result);
        when(mappingService.readInteger(responseEntity.getBody())).thenReturn(responseDto);
        doThrow(IncorrectStatusDto.class).when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.DELETE_ADDRESS_BY_ID);

        //when
        assertThatThrownBy(() -> addressRestService.deleteAddressById(ADDRESS_ID));

        //then
        verify(restService, times(1)).sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        verify(mappingService, times(1)).readInteger(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.DELETE_ADDRESS_BY_ID);
    }

    @Test
    void deleteAddressById_shouldBeDeleteAddressSuccessful() {
        //given
        var httpEntity = new HttpEntity<>(ADDRESS_ID);
        var responseEntity = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restService.sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity)).thenReturn(responseEntity);
        doNothing().when(responseValidationService)
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        var expectedResult = 1;
        var responseDto = ResponseDto.okResponseDto(expectedResult);
        when(mappingService.readInteger(responseEntity.getBody())).thenReturn(responseDto);
        doNothing().when(responseValidationService)
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.DELETE_ADDRESS_BY_ID);

        //when
        var actualResult = addressRestService.deleteAddressById(ADDRESS_ID);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(restService, times(1)).sendDelete(addressUrl + "/" + ADDRESS_ID, httpEntity);
        verify(responseValidationService, times(1))
                .throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        verify(mappingService, times(1)).readInteger(responseEntity.getBody());
        verify(responseValidationService, times(1))
                .throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.DELETE_ADDRESS_BY_ID);
    }

    private AddressDto createAddressDto() {
        return AddressDto.builder()
                .address(ADDRESS)
                .build();
    }
}