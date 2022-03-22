package com.usertest.service.userservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.restservice.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final RestService restService;

    private final UserRepository userRepository;

    private final NumberRepository numberRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(long id) {
        UserEntity userEntity = userRepository.getUserById(id);
        var responseAddressDto = restService.getAddressById(userEntity.getAddressId());
        List<String> numberEntitiesList = numberRepository.getNumbersByUserId(id);
        return userMapper.toUserDto(userEntity, numberEntitiesList, responseAddressDto.getData());
    }

    @Override
    public List<UserDto> getUsersByFilters(String partOfName, String partOfNumber) {
        var userEntities = userRepository.getUsersByFilters(partOfName);
        var numbersMap = numberRepository.getNumbersByFilter(partOfNumber);
        var userDtoList = new ArrayList<UserDto>();
        for (UserEntity userEntity: userEntities) {
            var currentUserId = userEntity.getId();
            if (numbersMap.containsKey(currentUserId)) {
                var addressDto = restService.getAddressById(userEntity.getAddressId());
                var numbers = numbersMap.get(currentUserId);
                var userDto = userMapper.toUserDto(userEntity, numbers, addressDto.getData());
                userDtoList.add(userDto);
            }
        }
        return userDtoList;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        var responseAddressDto = restService.saveAddress(userDto.getAddress());
        userDto.getAddress().setId(responseAddressDto.getData().getId());
        UserEntity userEntity = userRepository.saveUser(userMapper.toUserEntity(userDto));
        numberRepository.saveNumbersList(userDto.getNumbers(), userEntity.getId());
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), responseAddressDto.getData());
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        UserEntity userEntity = userRepository.updateUser(userDto);
        numberRepository.deleteNumbersByUserId(userId);
        numberRepository.saveNumbersList(userDto.getNumbers(), userId);
        var addressResult = restService.updateAddress(userDto.getAddress());
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), addressResult.getData());
    }

    @Override
    public int deleteUserById(long id) {
        var userDto = userRepository.getUserById(id);
        int result = userRepository.deleteUserById(id);
        numberRepository.deleteNumbersByUserId(id);
        restService.deleteAddressById(userDto.getAddressId());
        return result;
    }
}
