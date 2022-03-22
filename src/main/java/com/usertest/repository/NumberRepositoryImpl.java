package com.usertest.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class NumberRepositoryImpl implements NumberRepository{

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public int[] saveNumbersList(List<String> numbers, long userId) {
        var sql = "INSERT INTO number (number, user_id) VALUES " +
                "(:number, :user_id)";

        MapSqlParameterSource[] sqlParametersSource = numbers.stream()
                .map(number -> new MapSqlParameterSource()
                        .addValue("number", number)
                        .addValue("user_id", userId))
                .toArray(MapSqlParameterSource[]::new);

        return namedJdbcTemplate.batchUpdate(sql, sqlParametersSource);
    }

    @Override
    public List<String> getNumbersByUserId(long userId) {
        var sql = "SELECT number FROM number WHERE user_id=:userId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        return namedJdbcTemplate.query(sql, parameters, (rs, rowNum) -> rs.getString("number"));
    }

    @Override
    public int deleteNumbersByUserId(long userId) {
        var sql = "DELETE FROM number WHERE user_id=:userId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        return namedJdbcTemplate.update(sql, parameters);
    }
}
