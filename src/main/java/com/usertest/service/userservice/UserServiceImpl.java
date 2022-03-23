package com.usertest.service.userservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import com.usertest.exception.NotFoundNumberException;
import com.usertest.exception.NotFoundUserException;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.restservice.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        UserEntity userEntity = userRepository.getUserById(id).orElseThrow(() ->
                new NotFoundUserException("There is no user with ID = " + id + " in database."));
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

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        var addressDto = new AddressDto();
        addressDto.setAddress(userDto.getAddress());
        var responseAddressDto = restService.findOrInsertAddress(addressDto);
        UserEntity userEntity = userRepository.saveUser(userMapper.toUserEntity(userDto, responseAddressDto.getData().getId()));
        numberRepository.saveNumbersList(userDto.getNumbers(), userEntity.getId());
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), responseAddressDto.getData());
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        var addressDto = new AddressDto();
        addressDto.setAddress(userDto.getAddress());
        userRepository.getUserById(userId).orElseThrow(() ->
                new NotFoundUserException("There is no user with ID = " + userId + " in database."));
        var addressResult = restService.findOrInsertAddress(addressDto);

        UserEntity userEntity = userRepository.updateUser(userMapper.toUserEntity(userDto, addressResult.getData().getId()));
        List<String> existNumbers = numberRepository.getNumbersByUserId(userId);
        if(existNumbers != null && !existNumbers.isEmpty()) {
            int deleteNumbersResult = numberRepository.deleteNumbersByUserId(userId);
            if (deleteNumbersResult == 0) {
                throw new NotFoundNumberException("There is no numbers with user ID = " + userId + " in database.");
            }
        }
        if (userDto.getNumbers() != null && !userDto.getNumbers().isEmpty()) {
            numberRepository.saveNumbersList(userDto.getNumbers(), userId);
        }

        return userMapper.toUserDto(userEntity, userDto.getNumbers(), addressResult.getData());
    }

    @Transactional
    @Override
    public int deleteUserById(long userId) {
        var userDto = userRepository.getUserById(userId).orElseThrow(() ->
                new NotFoundUserException("There is no user with ID = " + userId + " in database."));
        int result = userRepository.deleteUserById(userId);
        if (result == 0) {
            throw new NotFoundUserException("There is no user with ID = " + userId + " in database.");
        }
        List<String> existNumbers = numberRepository.getNumbersByUserId(userId);
        if (existNumbers != null && !existNumbers.isEmpty()) {
            int deleteNumbersResult = numberRepository.deleteNumbersByUserId(userId);
            if (deleteNumbersResult == 0) {
                throw new NotFoundNumberException("There is no numbers with user ID = " + userId + " in database.");
            }
        }
        restService.deleteAddressById(userDto.getAddressId());
        return result;
    }
}
