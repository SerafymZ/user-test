package com.usertest.repository;

import java.util.List;

public interface NumberRepository {
    int[] saveNumbersList(List<String> numbers, long userId);
    List<String> getNumbersByUserId(long userId);
    int deleteNumbersByUserId(long userId);
}
