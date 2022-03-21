package com.usertest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private long id;
    private String name;
    private int age;
    private long addressId;

    public MapSqlParameterSource getSqlParams() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        if(id != 0) {
            params.addValue("id", id);
        }
        params.addValue("name", name);
        params.addValue("age", age);
        params.addValue("address_id", addressId);
        return params;
    }
}
