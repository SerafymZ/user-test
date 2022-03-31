package com.usertest.service.addressrestservice;

import com.usertest.dto.AddressDto;

public interface AddressRestService {
    AddressDto findOrInsertAddress(AddressDto addressDto) ;
    AddressDto getAddressById(long addressId);
    Integer deleteAddressById(long addressId) ;
}
