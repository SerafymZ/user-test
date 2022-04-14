package com.usertest.service.restservice;

import com.usertest.dto.basedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<String> sendPost(String url, HttpEntity<T> httpEntity) {
        return sendRequest(url, HttpMethod.POST, httpEntity);
    }

    @Override
    public <T> ResponseEntity<String> sendGet(String url, HttpEntity<T> httpEntity) {
        return sendRequest(url, HttpMethod.GET, httpEntity);
    }

    @Override
    public <T> ResponseEntity<String> sendDelete(String url, HttpEntity<T> httpEntity) {
        return sendRequest(url, HttpMethod.DELETE, httpEntity);
    }

    private <T> ResponseEntity<String> sendRequest(String url, HttpMethod httpMethod, HttpEntity<T> httpEntity) {
        try {
            return restTemplate.exchange(
                    url,
                    httpMethod,
                    httpEntity,
                    String.class
            );
        } catch (RestClientResponseException exception) {
            return new ResponseEntity(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
