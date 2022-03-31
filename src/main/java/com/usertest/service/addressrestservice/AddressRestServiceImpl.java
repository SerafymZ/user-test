package com.usertest.service.addressrestservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.mappingservice.MappingService;
import com.usertest.service.responsevalidationservice.AddressOperationType;
import com.usertest.service.responsevalidationservice.ResponseValidationService;
import com.usertest.service.restservice.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressRestServiceImpl implements AddressRestService{

    @Value("${address-service.url}")
    private String addressUrl;

    private final RestService restService;

    private final MappingService mappingService;

    private final ResponseValidationService responseValidationService;

    @Override
    public AddressDto findOrInsertAddress(AddressDto addressDto) {
        ResponseEntity<String> responseEntity = restService.sendPost(addressUrl, new HttpEntity<>(addressDto));
        responseValidationService.throwIfResponseEntityNotValid(
                responseEntity,
                AddressOperationType.FIND_OR_INSERT_ADDRESS
        );
        String body = responseEntity.getBody();
        ResponseDto<AddressDto> responseAddressDto = mappingService.readAddressDto(body);
        responseValidationService.throwIfStatusResponseDtoNotValid(
                responseAddressDto,
                AddressOperationType.FIND_OR_INSERT_ADDRESS
        );
        return responseAddressDto.getData();
    }

    @Override
    public AddressDto getAddressById(long addressId) {
        ResponseEntity<String> responseEntity =
                restService.sendGet(addressUrl + "/" +addressId, new HttpEntity<>(addressId));
        responseValidationService.throwIfResponseEntityNotValid(responseEntity, AddressOperationType.GET_ADDRESS_BY_ID);
        String body = responseEntity.getBody();
        ResponseDto<AddressDto> responseAddressDto = mappingService.readAddressDto(body);
        responseValidationService.throwIfStatusResponseDtoNotValid(
                responseAddressDto,
                AddressOperationType.GET_ADDRESS_BY_ID
        );
        return responseAddressDto.getData();
    }

    @Override
    public Integer deleteAddressById(long addressId) {
        ResponseEntity<String> responseEntity =
                restService.sendDelete(addressUrl + "/" +addressId, new HttpEntity<>(addressId));
        responseValidationService.throwIfResponseEntityNotValid(responseEntity, AddressOperationType.DELETE_ADDRESS_BY_ID);
        String body = responseEntity.getBody();
        ResponseDto<Integer> responseDto = mappingService.readInteger(body);
        responseValidationService.throwIfStatusResponseDtoNotValid(responseDto, AddressOperationType.DELETE_ADDRESS_BY_ID);
        return responseDto.getData();
    }
}
