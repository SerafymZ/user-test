package com.usertest.service.userservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(long id);
    List<UserDto> getUsersByFilters(String partOfName, String partOfNumber);
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(long userId,UserDto userDto);
    int deleteUserById(long id);

}
