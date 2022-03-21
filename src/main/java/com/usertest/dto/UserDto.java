package com.usertest.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String name;
    private int age;
    private String[] numbers;
    private AddressDto address;
}
