package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestServiceImpl implements RestService{

    private static final String ADDRESS_URL = "http://localhost:8888/address";

    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> findOrInsertAddress(AddressDto addressDto) {
        var httpEntity = new HttpEntity<>(addressDto);
        return restTemplate.exchange(
                ADDRESS_URL,
                HttpMethod.POST,
                httpEntity,
                String.class);
    }

    @Override
    public ResponseEntity<String> getAddressById(long addressId) {
        var httpEntity = new HttpEntity<>(addressId);
        return restTemplate.exchange(
                ADDRESS_URL + "/" +addressId,
                HttpMethod.GET,
                httpEntity,
                String.class);
    }

    @Override
    public ResponseEntity<String> deleteAddressById(long addressId) {
        var httpEntity = new HttpEntity<>(addressId);
        return restTemplate.exchange(
                ADDRESS_URL + "/" +addressId,
                HttpMethod.DELETE,
                httpEntity,
                String.class);
    }

    @Override
    public ResponseEntity<String> updateAddress(AddressDto addressDto) {
        var httpEntity = new HttpEntity<>(addressDto);
        return restTemplate.exchange(
                ADDRESS_URL,
                HttpMethod.PUT,
                httpEntity,
                String.class);
    }
}
