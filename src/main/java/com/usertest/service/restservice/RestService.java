package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;

public interface RestService {
    ResponseDto<AddressDto> saveAddress(AddressDto addressDto);
    ResponseDto<AddressDto> getAddressById(long addressId);
    ResponseDto<Integer> deleteAddressById(long addressId);
    ResponseDto<AddressDto>updateAddress(AddressDto addressDto);
}
