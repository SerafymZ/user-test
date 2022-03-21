package com.usertest.repository;

import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final String USER_ID = "userId";

    @Autowired
    NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        var sql = "INSERT INTO [user] (name, age, address_id) OUTPUT inserted.* VALUES " +
                "(:name, :age, :address_id)";

        return namedJdbcTemplate.queryForObject(sql, userEntity.getSqlParams(), new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public UserEntity getUserById(long id) {
        var sql = "SELECT id, [name], age, address_id FROM [user] WHERE id=:userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(USER_ID, id);
        return namedJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public List<UserEntity> getUsersByFilters(String partOfName) {
        var sql = "SELECT id AS user_id, name, age, address_id FROM [user] where name LIKE '%" + partOfName +"%'";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("partOfName", partOfName);
        var queryResult = namedJdbcTemplate.queryForList(sql, params);
        return queryResult.stream().map(map -> new UserEntity(
                        (long) map.get("user_id"),
                        (String) map.get("name"),
                        (int) map.get("age"),
                        (long) map.get("address_id")
                    )
                ).collect(Collectors.toList());
    }

    @Override
    public UserEntity updateUser(UserDto userDto) {
        var sql = "UPDATE [user] SET name=:userName, age=:userAge, address_id=:addressId " +
                "OUTPUT inserted.* " +
                "WHERE id=:userId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(USER_ID, userDto.getId());
        parameters.addValue("userName", userDto.getName());
        parameters.addValue("userAge", userDto.getAge());
        parameters.addValue("addressId", userDto.getAddress().getId());
        return namedJdbcTemplate.queryForObject(sql, parameters, new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public int deleteUserById(long id) {
        var sql = "DELETE FROM [user] where id=:userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(USER_ID, id);
        return namedJdbcTemplate.update(sql, params);
    }
}
