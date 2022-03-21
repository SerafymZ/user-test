package com.usertest.mapper;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;

import java.util.List;

public interface UserMapper {
    UserEntity toUserEntity(UserDto userDto);

    UserDto toUserDto(UserEntity userEntity, List<String> numbers, AddressDto addressDto);
}
