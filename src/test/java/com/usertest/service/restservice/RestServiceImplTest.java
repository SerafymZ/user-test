package com.usertest.service.restservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RestServiceImpl.class)
class RestServiceImplTest {

    private static final String URL = "test/url";
    private static final String BODY = "body";
    private static final String ADDRESS = "Canada";
    private static final Long ID = 1L;

    @Autowired
    RestServiceImpl restService;

    @MockBean
    RestTemplate restTemplate;

    @Test
    void sendPost_shouldBeReturnedFailedResponseDto() {
        //given
        var addressDto = new AddressDto(ID, ADDRESS);
        var httpEntity = new HttpEntity<>(addressDto);
        doThrow(RestClientResponseException.class)
                .when(restTemplate).exchange(URL, HttpMethod.POST, httpEntity, String.class);
        var expectedResult = new ResponseEntity(ResponseDto.failedResponseDto(null), HttpStatus.BAD_REQUEST);

        //when
        var actualResult = restService.sendPost(URL, addressDto);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(restTemplate, times(1)).exchange(URL, HttpMethod.POST, httpEntity, String.class);
    }

    @Test
    void sendPost_shouldBeSendPostSuccessful() {
        //given
        var addressDto = new AddressDto(ID, ADDRESS);
        var httpEntity = new HttpEntity<>(addressDto);
        var expectedResult = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class)).thenReturn(expectedResult);

        //when
        var actualResult = restService.sendPost(URL, addressDto);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(restTemplate, times(1)).exchange(URL, HttpMethod.POST, httpEntity, String.class);
    }

    @Test
    void sendGet_shouldBeThrewRestClientResponseException() {
        //given
        var headers = new HttpHeaders();
        headers.set("methodName", "Get");
        var httpEntity = new HttpEntity<>(headers);
        doThrow(RestClientResponseException.class)
                .when(restTemplate).exchange(URL, HttpMethod.GET, httpEntity, String.class);
        var expectedResult = new ResponseEntity(ResponseDto.failedResponseDto(null), HttpStatus.BAD_REQUEST);

        //when
        var actualResult = restService.sendGet(URL);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(restTemplate, times(1)).exchange(URL, HttpMethod.GET, httpEntity, String.class);
    }

    @Test
    void sendGet_shouldBeSendGetSuccess() {
        //given
        var headers = new HttpHeaders();
        headers.set("methodName", "Get");
        var httpEntity = new HttpEntity<>(headers);
        var expectedResult = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restTemplate.exchange(URL, HttpMethod.GET, httpEntity, String.class)).thenReturn(expectedResult);

        //when
        var actualResult = restService.sendGet(URL);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(restTemplate, times(1)).exchange(URL, HttpMethod.GET, httpEntity, String.class);
    }

    @Test
    void sendDelete_shouldBeThrewRestClientResponseException() {
        //given
        var headers = new HttpHeaders();
        headers.set("methodName", "Delete");
        var httpEntity = new HttpEntity<>(headers);
        doThrow(RestClientResponseException.class)
                .when(restTemplate).exchange(URL, HttpMethod.DELETE, httpEntity, String.class);
        var expectedResult = new ResponseEntity(ResponseDto.failedResponseDto(null), HttpStatus.BAD_REQUEST);

        //when
        var actualResult = restService.sendDelete(URL);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(restTemplate, times(1)).exchange(URL, HttpMethod.DELETE, httpEntity, String.class);
    }

    @Test
    void sendDelete_shouldBeSendDeleteSuccessful() {
        //given
        var headers = new HttpHeaders();
        headers.set("methodName", "Delete");
        var httpEntity = new HttpEntity<>(headers);
        var expectedResult = new ResponseEntity<>(BODY, HttpStatus.OK);
        when(restTemplate.exchange(URL, HttpMethod.DELETE, httpEntity, String.class)).thenReturn(expectedResult);

        //when
        var actualResult = restService.sendDelete(URL);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(restTemplate, times(1)).exchange(URL, HttpMethod.DELETE, httpEntity, String.class);
    }
}