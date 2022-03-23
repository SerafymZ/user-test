package com.usertest.mapper;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper{
    @Override
    public UserEntity toUserEntity(UserDto userDto, Long addressId) {
        if (userDto == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();

        if(userDto.getId() != null) {
            userEntity.setId(userDto.getId());
        }

        if(userDto.getName() != null) {
            userEntity.setName(userDto.getName());
        }

        if(userDto.getAge() != null ) {
            userEntity.setAge(userDto.getAge());
        }

        if(addressId != null) {
            userEntity.setAddressId(addressId);
        }

        return userEntity;
    }

    @Override
    public UserDto toUserDto(UserEntity userEntity, List<String> numbers, AddressDto addressDto) {
        if(userEntity == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        if(userEntity.getId() != null) {
            userDto.setId(userEntity.getId());
        }

        if(userEntity.getName() != null) {
            userDto.setName(userEntity.getName());
        }

        if(userEntity.getAge() != null) {
            userDto.setAge(userEntity.getAge());
        }

        if(numbers != null) {
            userDto.setNumbers(numbers);
        }

        if(addressDto != null) {
            userDto.setAddress(addressDto.getAddress());
        }
        return userDto;
    }

    @Override
    public UserDto toUserDto(UserWithNumberEntity entity, AddressDto addressDto) {
        if(entity == null) {
            return null;
        }

        UserDto userDto = new UserDto();

        if (entity.getId() != null) {
            userDto.setId(entity.getId());
        }

        if(entity.getName() != null) {
            userDto.setName(entity.getName());
        }

        if(entity.getAge() != null) {
            userDto.setAge(entity.getAge());
        }

        if(entity.getNumber() != null) {
            ArrayList<String> numbers = new ArrayList<>();
            numbers.add(entity.getNumber());
            userDto.setNumbers(numbers);
        }

        if(addressDto != null) {
            userDto.setAddress(addressDto.getAddress());
        }

        return userDto;
    }
}
