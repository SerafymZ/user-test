package com.usertest.service.userservice;

import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.restservice.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
        var userEntities = userRepository.getUsersByFilters(partOfName, partOfNumber);
        HashMap<Long, UserDto> usersMap = new HashMap<>();
        for(UserWithNumberEntity entity : userEntities) {
            var addressDto = restService.getAddressById(entity.getAddressId());
            if (usersMap.containsKey(entity.getId())) {
                var userDto = usersMap.get(entity.getId());
                userDto.getNumbers().add(entity.getNumber());
            } else {
                var userDto = userMapper.toUserDto(entity, addressDto.getData());
                usersMap.put(entity.getId(), userDto);
            }
        }
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        var responseAddressDto = restService.findOrInsertAddress(userDto.getAddress());
        UserEntity userEntity = userRepository.saveUser(userMapper.toUserEntity(userDto, responseAddressDto.getData().getId()));
        numberRepository.saveNumbersList(userDto.getNumbers(), userEntity.getId());
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), responseAddressDto.getData());
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        UserEntity userEntity = userRepository.updateUser(userDto);
        numberRepository.deleteNumbersByUserId(userId);
        numberRepository.saveNumbersList(userDto.getNumbers(), userId);
        var addressResult = restService.findOrInsertAddress(userDto.getAddress());
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
