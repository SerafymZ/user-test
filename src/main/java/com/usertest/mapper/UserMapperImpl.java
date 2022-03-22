package com.usertest.mapper;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperImpl implements UserMapper{
    @Override
    public UserEntity toUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();

        if(userDto.getId() != 0) {
            userEntity.setId(userDto.getId());
        }

        if(userDto.getName() != null) {
            userEntity.setName(userDto.getName());
        }

        if(userDto.getAge() != 0 ) {
            userEntity.setAge(userDto.getAge());
        }

        if(userDto.getAddress() != null) {
            userEntity.setAddressId(userDto.getAddress().getId());
        }

        return userEntity;
    }

    @Override
    public UserDto toUserDto(UserEntity userEntity, List<String> numbers, AddressDto addressDto) {
        if(userEntity == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        if(userEntity.getId() != 0) {
            userDto.setId(userEntity.getId());
        }

        if(userEntity.getName() != null) {
            userDto.setName(userEntity.getName());
        }

        if(userEntity.getAge() != 0) {
            userDto.setAge(userEntity.getAge());
        }

        if(numbers != null) {
            userDto.setNumbers(numbers);
        }

        if(addressDto != null) {
            userDto.setAddress(addressDto);
        }
        return userDto;
    }
}
