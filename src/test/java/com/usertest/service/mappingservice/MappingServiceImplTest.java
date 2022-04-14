package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.exception.MappingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MappingServiceImpl.class)
class MappingServiceImplTest {

    private static final String BODY = "body";
    private static final String ADDRESS = "Canada";
    private static final Long ID = 1L;

    @Autowired
    MappingServiceImpl mappingService;

    @MockBean
    ObjectMapper objectMapper;

    @Test
    void readBody_shouldBeThrewJsonProcessingException() throws JsonProcessingException {
        //given
        var objectReaderMock = mock(ObjectReader.class);
        var typeReference = new TypeReference<>() {};
        when(objectMapper.readerFor(typeReference)).thenReturn(objectReaderMock);
        doThrow(JsonProcessingException.class).when(objectReaderMock).readValue(anyString());

        //when
        assertThatThrownBy(() -> mappingService.readBody(BODY, typeReference)).isInstanceOf(MappingException.class);

        //then
        verify(objectMapper, times(1)).readerFor(typeReference);
        verify(objectReaderMock, times(1)).readValue(anyString());
    }

    @Test
    void readBody_shouldBeReadValueSuccessful() throws JsonProcessingException {
        //given
        var objectReaderMock = mock(ObjectReader.class);
        var typeReference = new TypeReference<>() {};
        when(objectMapper.readerFor(typeReference)).thenReturn(objectReaderMock);
        var addressDto = new AddressDto(ID, ADDRESS);
        var expectedResult = ResponseDto.okResponseDto(addressDto);
        when(objectReaderMock.readValue(anyString())).thenReturn(expectedResult);

        //when
        var actualResult = mappingService.readBody(BODY, typeReference);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(objectMapper, times(1)).readerFor(typeReference);
        verify(objectReaderMock, times(1)).readValue(anyString());
    }
}