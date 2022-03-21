package com.usertest.repository;

import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    UserEntity saveUser(UserEntity userDto);
    UserEntity getUserById(long id);
    List<UserEntity> getUsersByFilters(String partOfName);
    UserEntity updateUser(UserDto userDto);
    int deleteUserById(long id);
}
