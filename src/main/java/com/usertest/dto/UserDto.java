package com.usertest.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private List<String> numbers;
    private AddressDto address;
}
