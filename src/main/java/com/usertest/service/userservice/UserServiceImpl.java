package com.usertest.service.userservice;

import com.usertest.counter.SavedUsersCounter;
import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import com.usertest.exception.NotFoundUserException;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.addressrestservice.AddressRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String NOT_FOUND_USER_MESSAGE = "There is no user with ID = %d in database.";

    private final AddressRestService addressRestService;

    private final UserRepository userRepository;

    private final NumberRepository numberRepository;

    private final UserMapper userMapper;

    private final SavedUsersCounter savedUsersCounter;

    @Override
    public UserDto getUserById(long id)  {
       UserWithNumberEntity userWithNumbersEntity = userRepository.getUserWithNumbersById(id).orElseThrow(() ->
                new NotFoundUserException(String.format(NOT_FOUND_USER_MESSAGE, id)));

        AddressDto responseAddressDto = addressRestService.getAddressById(userWithNumbersEntity.getAddressId());
        List<String> numbers = Arrays.stream(userWithNumbersEntity.getNumber().split(","))
                .collect(Collectors.toList());
        return userMapper.toUserDto(userWithNumbersEntity, numbers, responseAddressDto);
    }

    @Override
    public List<UserDto> getUsersByFilters(String partOfName, String partOfNumber) {
        List<UserWithNumberEntity> userEntities = userRepository.getUsersByFilters(partOfName, partOfNumber);
        HashMap<Long, UserDto> usersMap = new HashMap<>();
        for(UserWithNumberEntity entity : userEntities) {
            AddressDto addressDto = null;
            if (entity.getAddressId() != null) {
                addressDto = addressRestService.getAddressById(entity.getAddressId());
            }
            List<String> numbers = Arrays.stream(entity.getNumber().split(","))
                    .collect(Collectors.toList());
            var userDto = userMapper.toUserDto(entity, numbers, addressDto);
            var userDtoFromMap = usersMap.putIfAbsent(entity.getId(), userDto);
            if(userDtoFromMap != null) {
                userDtoFromMap.getNumbers().add(entity.getNumber());
            }
        }
        return new ArrayList<>(usersMap.values());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        var addressDto = new AddressDto(null, userDto.getAddress());
        AddressDto resultAddressDto = addressRestService.findOrInsertAddress(addressDto);
        UserEntity userEntity = userRepository.saveUser(
                userMapper.toUserEntity(userDto, resultAddressDto.getId())
        );
        numberRepository.saveNumbersList(userDto.getNumbers(), userEntity.getId());
        savedUsersCounter.onUserSave();
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), resultAddressDto);
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        userDto.setId(userId);
        userRepository.getUserWithNumbersById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format(NOT_FOUND_USER_MESSAGE, userId)));

        var addressDto = new AddressDto(null, userDto.getAddress());
        AddressDto resultAddressDto = addressRestService.findOrInsertAddress(addressDto);

        UserEntity userEntity = userRepository.updateUser(
                userMapper.toUserEntity(userDto, resultAddressDto.getId())
        );

        numberRepository.deleteNumbersByUserId(userId);
        numberRepository.saveNumbersList(userDto.getNumbers(), userId);

        return userMapper.toUserDto(userEntity, userDto.getNumbers(), resultAddressDto);
    }

    @Transactional
    @Override
    public int deleteUserById(long userId) {
        UserWithNumberEntity userWithNumberEntity = userRepository.getUserWithNumbersById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format(NOT_FOUND_USER_MESSAGE, userId)));
        List<String> existNumbers = numberRepository.getNumbersByUserId(userId);
        if (existNumbers != null && !existNumbers.isEmpty()) {
            numberRepository.deleteNumbersByUserId(userId);
        }
        int result = userRepository.deleteUserById(userId);
        Long addressId = userWithNumberEntity.getAddressId();
        if(addressId != null && userRepository.addressUsersCount(addressId) == 0) {
            addressRestService.deleteAddressById(addressId);
        }
        return result;
    }
}
