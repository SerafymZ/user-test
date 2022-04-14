package com.usertest.service.mappingservice;

import com.usertest.dto.basedto.ResponseDto;

public interface MappingService {
    <T> ResponseDto<T> readBody(String body, Class<T> contentClass);
}
