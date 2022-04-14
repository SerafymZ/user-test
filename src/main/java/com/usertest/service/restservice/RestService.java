package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface RestService {
    <T> ResponseEntity<String> sendPost(String url, HttpEntity<T> httpEntity);
    <T> ResponseEntity<String> sendGet(String url, HttpEntity<T> httpEntity);
    <T> ResponseEntity<String> sendDelete(String url, HttpEntity<T> httpEntity);
}
