package com.usertest.service.userservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(long id) throws JsonProcessingException;
    List<UserDto> getUsersByFilters(String partOfName, String partOfNumber) throws JsonProcessingException;
    UserDto saveUser(UserDto userDto) throws JsonProcessingException;
    UserDto updateUser(long userId,UserDto userDto) throws JsonProcessingException;
    int deleteUserById(long id) throws JsonProcessingException;

}
