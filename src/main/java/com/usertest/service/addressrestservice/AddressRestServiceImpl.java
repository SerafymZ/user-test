package com.usertest.service.addressrestservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.restservice.RestService;
import com.usertest.service.responsevalidationservice.AddressOperationType;
import com.usertest.service.responsevalidationservice.ResponseValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@RequiredArgsConstructor
@Service
public class AddressRestServiceImpl implements AddressRestService{

    private final RestService restService;

    private final ResponseValidationService responseValidationService;

    @Override
    public ResponseDto<AddressDto> findOrInsertAddress(AddressDto addressDto)
            throws ResourceAccessException, JsonProcessingException {
        ResponseEntity<String> responseEntity = restService.findOrInsertAddress(addressDto);
        responseValidationService.checkStatusResponseEntity(responseEntity, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        String body = responseEntity.getBody();
        ResponseDto<AddressDto> dto = new ObjectMapper().readValue(body, new TypeReference<>() {});
        responseValidationService.checkStatusResponseDto(dto, AddressOperationType.FIND_OR_INSERT_ADDRESS);
        return dto;
    }

    @Override
    public ResponseDto<AddressDto> getAddressById(long addressId)
            throws ResourceAccessException, JsonProcessingException {
        ResponseEntity<String> responseEntity = restService.getAddressById(addressId);
        responseValidationService.checkStatusResponseEntity(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);
        String body = responseEntity.getBody();
        ResponseDto<AddressDto> dto = new ObjectMapper().readValue(body, new TypeReference<>() {});
        responseValidationService.checkStatusResponseDto(dto, AddressOperationType.GET_ADDRESS_BY_ID);
        return dto;
    }

    @Override
    public ResponseDto<Integer> deleteAddressById(long addressId)
            throws ResourceAccessException, JsonProcessingException {
        ResponseEntity<String> responseEntity = restService.deleteAddressById(addressId);
        responseValidationService.checkStatusResponseEntity(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        String body = responseEntity.getBody();
        ResponseDto<Integer> dto = new ObjectMapper().readValue(body, new TypeReference<>() {});
        responseValidationService.checkStatusResponseDto(dto, AddressOperationType.DELETE_ADDRESS_BY_ID);
        return dto;
    }

    @Override
    public ResponseDto<AddressDto> updateAddress(AddressDto addressDto)
            throws ResourceAccessException, JsonProcessingException {
        ResponseEntity<String> responseEntity = restService.updateAddress(addressDto);
        responseValidationService.checkStatusResponseEntity(responseEntity, AddressOperationType.UPDATE_ADDRESS);
        String body = responseEntity.getBody();
        ResponseDto<AddressDto> dto = new ObjectMapper().readValue(body, new TypeReference<>() {});
        responseValidationService.checkStatusResponseDto(dto, AddressOperationType.UPDATE_ADDRESS);
        return dto;
    }
}
