package com.usertest.service.restservice;

import com.usertest.dto.basedto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<String> sendPost(String url, T body) {
        return sendRequest(url, HttpMethod.POST, new HttpEntity<>(body));
    }

    @Override
    public <T> ResponseEntity<String> sendGet(String url) {
        var headers = new HttpHeaders();
        headers.set("methodName", "Get");
        return sendRequest(url, HttpMethod.GET, new HttpEntity<>(headers));
    }

    @Override
    public <T> ResponseEntity<String> sendDelete(String url) {
        var headers = new HttpHeaders();
        headers.set("methodName", "Delete");
        return sendRequest(url, HttpMethod.DELETE, new HttpEntity<>(headers));
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
