package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface RestService {
    ResponseEntity<String> sendPost(String url, HttpEntity<AddressDto> httpEntity);
    ResponseEntity<String> sendGet(String url, HttpEntity<Long> httpEntity);
    ResponseEntity<String> sendDelete(String url, HttpEntity<Long> httpEntity);
}
