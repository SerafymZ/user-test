package com.usertest.repository;

import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;

import java.util.List;

public interface UserRepository {
    UserEntity saveUser(UserEntity userDto);
    UserEntity getUserById(long id);
    List<UserWithNumberEntity> getUsersByFilters(String partOfName, String partOfNumber);
    UserEntity updateUser(UserDto userDto);
    int deleteUserById(long id);
}
