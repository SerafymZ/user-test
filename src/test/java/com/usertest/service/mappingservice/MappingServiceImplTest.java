package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    void readAddressDto_shouldBeThrewJsonProcessingException() throws JsonProcessingException {
        //given
        doThrow(JsonProcessingException.class).when(objectMapper).readValue(anyString(), isA(TypeReference.class));

        //when
        assertThatThrownBy(() -> mappingService.readAddressDto(BODY)).isInstanceOf(MappingException.class);

        //then
        verify(objectMapper, times(1)).readValue(anyString(), isA(TypeReference.class));
    }

    @Test
    void readAddressDto_shouldBeReadValueSuccessful() throws JsonProcessingException {
        //given
        var addressDto = createAddressDto();
        var expectedResult = ResponseDto.okResponseDto(addressDto);
        when(objectMapper.readValue(anyString(), isA(TypeReference.class))).thenReturn(expectedResult);

        //when
        var actualResult = mappingService.readAddressDto(BODY);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(objectMapper, times(1)).readValue(anyString(), isA(TypeReference.class));
    }

    @Test
    void readInteger() throws JsonProcessingException {
        //given
        doThrow(JsonProcessingException.class).when(objectMapper).readValue(anyString(), isA(TypeReference.class));

        //when
        assertThatThrownBy(() -> mappingService.readInteger(BODY)).isInstanceOf(MappingException.class);

        //then
        verify(objectMapper, times(1)).readValue(anyString(), isA(TypeReference.class));
    }

    @Test
    void readInteger_() throws JsonProcessingException {
        //given
        var result = 1;
        var expectedResult = ResponseDto.okResponseDto(result);
        when(objectMapper.readValue(anyString(), isA(TypeReference.class))).thenReturn(expectedResult);

        //when
        var actualResult = mappingService.readInteger(BODY);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(objectMapper, times(1)).readValue(anyString(), isA(TypeReference.class));
    }

    private AddressDto createAddressDto() {
        return AddressDto.builder()
                .id(ID)
                .address(ADDRESS)
                .build();
    }
}