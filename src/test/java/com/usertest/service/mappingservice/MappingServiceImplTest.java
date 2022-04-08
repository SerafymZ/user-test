package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
    void readAddressDto_() throws JsonProcessingException {
        //given
        var addressDto = createAddressDto();
        var expectedResult = ResponseDto.okResponseDto(addressDto);
        var typeReference = new TypeReference<ResponseDto<AddressDto>>() {};
        doThrow(JsonProcessingException.class).when(objectMapper).readValue(BODY, typeReference);

        //when
        assertThatThrownBy(() -> mappingService.readAddressDto(BODY)).isInstanceOf(JsonProcessingException.class);

        //then
        verify(objectMapper, times(1)).readValue(BODY, typeReference);
    }

    @Test
    void readAddressDto() throws JsonProcessingException {
        //given
        var addressDto = createAddressDto();
        var expectedResult = ResponseDto.okResponseDto(addressDto);
        var typeReference = new TypeReference<ResponseDto<AddressDto>>() {};
        when(objectMapper.readValue(BODY, typeReference)).thenReturn(expectedResult);

        //when
        var actualResult = mappingService.readAddressDto(BODY);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(objectMapper, times(1)).readValue(BODY, typeReference);
    }

    @Test
    void readInteger() {
    }

    private AddressDto createAddressDto() {
        return AddressDto.builder()
                .id(ID)
                .address(ADDRESS)
                .build();
    }
}