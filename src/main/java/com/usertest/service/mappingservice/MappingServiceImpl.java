package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.exception.MappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MappingServiceImpl implements MappingService{

    private final ObjectMapper objectMapper;

    @Override
    public ResponseDto<AddressDto> readAddressDto(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<ResponseDto<AddressDto>>() {});
        } catch (JsonProcessingException e) {
            throw new MappingException("Failed to read address. body: " + body);
        }
    }

    @Override
    public ResponseDto<Integer> readInteger(String body) {
        try {
            return objectMapper.readValue(body, new TypeReference<ResponseDto<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new MappingException("Failed to read integer value. body: " + body);
        }
    }
}
