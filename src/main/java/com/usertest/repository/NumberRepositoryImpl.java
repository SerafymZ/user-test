package com.usertest.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Map<Long, List<String>> getNumbersByFilter(String partOfNumber) {
        var sql = "SELECT id , number, user_id FROM number AS num WHERE number LIKE '%" + partOfNumber +"%'";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("partOfName", partOfNumber);
        var queryResult = namedJdbcTemplate.queryForList(sql, params);
        var result = new HashMap<Long, List<String>>();
        for (Map<String, Object> line: queryResult) {
            var currentUserId = (Long) line.get("user_id");
            var currentNumber = (String) line.get("number");
            if(result.containsKey(currentUserId)) {
                var numbers = result.get(currentUserId);
                numbers.add(currentNumber);
            } else {
                var numbers = new ArrayList<String>();
                numbers.add(currentNumber);
                result.put(currentUserId, numbers);
            }
        }
        return result;
    }
}
