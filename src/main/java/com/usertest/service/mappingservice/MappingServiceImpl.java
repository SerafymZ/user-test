package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.exception.MappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MappingServiceImpl implements MappingService{

    private final ObjectMapper objectMapper;

    @Override
    public <T> ResponseDto<T> readBody(String body, Class<T> contentClass) {
        try {
//            return objectMapper.readValue(body, new TypeReference<ResponseDto<T>>() {});

            JavaType type = objectMapper.getTypeFactory().constructParametricType(ResponseDto.class, contentClass);
            return objectMapper.readValue(body, type);

//            TypeReference<T> typeReference = new TypeReference<>() {};
//            return objectMapper.readerFor(typeReference).readValue(body);
        } catch (JsonProcessingException e) {
            throw new MappingException("Failed to read address. body: " + body);
        }
    }
}
