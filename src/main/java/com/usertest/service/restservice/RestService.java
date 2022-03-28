package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import org.springframework.http.ResponseEntity;

public interface RestService {
    ResponseEntity<String> findOrInsertAddress(AddressDto addressDto);
    ResponseEntity<String> getAddressById(long addressId);
    ResponseEntity<String> deleteAddressById(long addressId);
    ResponseEntity<String>updateAddress(AddressDto addressDto);
}
