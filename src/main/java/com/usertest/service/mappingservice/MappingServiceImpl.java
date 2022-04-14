package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.exception.MappingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MappingServiceImpl implements MappingService{

    private final ObjectMapper objectMapper;

    @Override
    public <T> T readBody(String body, TypeReference<T> typeReference) {
        try {
            return objectMapper.readerFor(typeReference).readValue(body);
        } catch (JsonProcessingException e) {
            throw new MappingException("Failed to read address. body: " + body);
        }
    }
}
