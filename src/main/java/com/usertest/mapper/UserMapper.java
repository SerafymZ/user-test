package com.usertest.mapper;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserEntity toUserEntity(UserDto userDto, Long addressId);

    @Mapping(source = "userEntity.id", target = "id")
    @Mapping(source = "userEntity.name", target = "name")
    @Mapping(source = "userEntity.age", target = "age")
    @Mapping(source = "numbers", target = "numbers")
    @Mapping(source = "addressDto.address", target = "address")
    UserDto toUserDto(UserEntity userEntity, List<String> numbers, AddressDto addressDto);

    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.age", target = "age")
    @Mapping(source = "numbers", target = "numbers")
    @Mapping(source = "addressDto.address", target = "address")
    UserDto toUserDto(UserWithNumberEntity entity, List<String> numbers, AddressDto addressDto);
}
