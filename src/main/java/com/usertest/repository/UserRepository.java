package com.usertest.repository;

import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity saveUser(UserEntity userDto);
    Optional<UserWithNumberEntity> getUserWithNumbersById(long id);
    List<UserWithNumberEntity> getUsersByFilters(String partOfName, String partOfNumber);
    UserEntity updateUser(UserEntity userEntity);
    int deleteUserById(long id);
    int addressUsersCount(long addressId);
}
