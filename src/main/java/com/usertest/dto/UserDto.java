package com.usertest.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;

    @NotBlank
    @Size(max = 50, message = "Name size must be less than 50 symbols.")
    private String name;
    @Max(value = 130, message = "Age must be less then 130.")
    private Integer age;

    private List<String> numbers;

    @Size(max = 50, message = "Address size must be less than 50 symbols.")
    private String address;
}
