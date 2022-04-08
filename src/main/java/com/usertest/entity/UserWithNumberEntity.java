package com.usertest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithNumberEntity {
    private Long id;
    private String name;
    private Integer age;
    private Long addressId;
    private String number;
}
