package com.usertest.service.mappingservice;

import com.fasterxml.jackson.core.type.TypeReference;

public interface MappingService {
    <T> T readBody(String body, TypeReference<T> typeReference);
}
