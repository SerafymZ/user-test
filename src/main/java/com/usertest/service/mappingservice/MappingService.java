package com.usertest.service.mappingservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.basedto.ResponseDto;

public interface MappingService {
    ResponseDto<AddressDto> readAddressDto(String body);
    ResponseDto<Integer> readInteger(String body);
}
