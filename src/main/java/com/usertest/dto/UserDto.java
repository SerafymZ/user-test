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
    @Size(max = 50, message = "{user.field.name.exceeded-size}")
    private String name;
    @Max(value = 130, message = "{user.field.age.exceeded-value}")
    private Integer age;

    private List<String> numbers;

    @Size(max = 50, message = "{user.field.address.exceeded-size}")
    private String address;
}
