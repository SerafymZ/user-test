package com.usertest.service.restservice;

import org.springframework.http.ResponseEntity;

public interface RestService {
    <T> ResponseEntity<String> sendPost(String url, T body);
    <T> ResponseEntity<String> sendGet(String url);
    <T> ResponseEntity<String> sendDelete(String url);
}
