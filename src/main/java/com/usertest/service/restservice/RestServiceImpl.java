package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
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
    public ResponseEntity<String> sendPost(String url, HttpEntity<AddressDto> httpEntity) {
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
        } catch (RestClientResponseException exception) {
            return new ResponseEntity(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public ResponseEntity<String> sendGet(String url, HttpEntity<Long> httpEntity) {
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );
        } catch (RestClientResponseException exception) {
            return new ResponseEntity(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public ResponseEntity<String> sendDelete(String url, HttpEntity<Long> httpEntity) {
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    httpEntity,
                    String.class
            );
        } catch (RestClientResponseException exception) {
            return new ResponseEntity(ResponseDto.failedResponseDto(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
