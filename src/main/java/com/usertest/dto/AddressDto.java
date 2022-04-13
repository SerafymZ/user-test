package com.usertest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String address;
}
