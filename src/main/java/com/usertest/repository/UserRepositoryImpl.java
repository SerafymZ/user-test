package com.usertest.repository;

import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final String USER_ID = "userId";

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        var sql = "INSERT INTO [user] (name, age, address_id) OUTPUT inserted.* VALUES " +
                "(:name, :age, :address_id)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", userEntity.getName());
        params.addValue("age", userEntity.getAge());
        params.addValue("address_id", userEntity.getAddressId());
        return namedJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(UserEntity.class));
    }

    @Override
    public Optional<UserEntity> getUserById(long id) {
        var sql = "SELECT id, [name], age, address_id FROM [user] WHERE id=:userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(USER_ID, id);
        UserEntity userEntity;
        try {
            userEntity = namedJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(UserEntity.class));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
        return Optional.of(userEntity);
    }

    @Override
    public List<UserWithNumberEntity> getUsersByFilters(String partOfName, String partOfNumber) {
        var sql = "SELECT u.id id, u.name name, u.age age, u.address_id address_id, n.number number FROM \n" +
                "[user] AS u JOIN number AS n ON u.id = n.user_id\n" +
                "WHERE name LIKE '%' + :partOfName + '%' AND number LIKE '%' + :partOfNumber + '%'";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("partOfName", partOfName);
        params.addValue("partOfNumber", partOfNumber);
        return namedJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(UserWithNumberEntity.class));
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        var sql = "UPDATE [user] SET name=:userName, age=:userAge, address_id=:addressId " +
                "OUTPUT inserted.* " +
                "WHERE id=:userId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(USER_ID, userEntity.getId());
        parameters.addValue("userName", userEntity.getName());
        parameters.addValue("userAge", userEntity.getAge());
        parameters.addValue("addressId", userEntity.getAddressId());
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
