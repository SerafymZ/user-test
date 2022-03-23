package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestServiceImpl implements RestService{

    private static final String ADDRESS_URL = "http://localhost:8888/address";

    private final RestTemplate restTemplate;

    @Override
    public ResponseDto<AddressDto> findOrInsertAddress(AddressDto addressDto) {
        var httpEntity = new HttpEntity<>(addressDto);
        var result = restTemplate.exchange(
                ADDRESS_URL,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<ResponseDto<AddressDto>>(){});
        return result.getBody();
    }

    @Override
    public ResponseDto<AddressDto> getAddressById(long addressId) {
        var httpEntity = new HttpEntity<>(addressId);
        var result = restTemplate.exchange(
                ADDRESS_URL + "/" +addressId,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<ResponseDto<AddressDto>>(){});
        return result.getBody();
    }

    @Override
    public ResponseDto<Integer> deleteAddressById(long addressId) {
        var httpEntity = new HttpEntity<>(addressId);
        var result = restTemplate.exchange(
                ADDRESS_URL + "/" +addressId,
                HttpMethod.DELETE,
                httpEntity,
                new ParameterizedTypeReference<ResponseDto<Integer>>(){});
        return result.getBody();
    }

    @Override
    public ResponseDto<AddressDto> updateAddress(AddressDto addressDto) {
        var httpEntity = new HttpEntity<>(addressDto);
        var result = restTemplate.exchange(
                ADDRESS_URL,
                HttpMethod.PUT,
                httpEntity,
                new ParameterizedTypeReference<ResponseDto<AddressDto>>(){});
        return result.getBody();
    }
}
