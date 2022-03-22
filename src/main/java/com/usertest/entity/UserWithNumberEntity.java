package com.usertest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithNumberEntity {
    private Long id;
    private String name;
    private Integer age;
    private Long addressId;
    private String number;
}
