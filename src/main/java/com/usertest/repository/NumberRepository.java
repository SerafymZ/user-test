package com.usertest.repository;

import java.util.List;
import java.util.Map;

public interface NumberRepository {
    int[] saveNumbersList(List<String> numbers, long userId);
    List<String> getNumbersByUserId(long userId);
    int deleteNumbersByUserId(long userId);
    int[] updateNumbers(long userId, String[] numbers);
    Map<Long, List<String>> getNumbersByFilter(String partOfNumber);

}
