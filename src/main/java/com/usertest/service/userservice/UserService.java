package com.usertest.service.userservice;

import com.usertest.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(long id);
    List<UserDto> getUsersByFilters(String partOfName, String partOfNumber);
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    int deleteUserById(long id);

}
