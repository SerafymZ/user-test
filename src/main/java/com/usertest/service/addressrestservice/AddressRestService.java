package com.usertest.service.addressrestservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;

public interface AddressRestService {
    ResponseDto<AddressDto> findOrInsertAddress(AddressDto addressDto) throws JsonProcessingException;
    ResponseDto<AddressDto> getAddressById(long addressId) throws JsonProcessingException;
    ResponseDto<Integer> deleteAddressById(long addressId) throws JsonProcessingException;
    ResponseDto<AddressDto>updateAddress(AddressDto addressDto) throws JsonProcessingException;
}
