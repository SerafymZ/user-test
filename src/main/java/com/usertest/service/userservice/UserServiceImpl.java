package com.usertest.service.userservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import com.usertest.exception.IdValuesDoNotMatchException;
import com.usertest.exception.NotFoundNumberException;
import com.usertest.exception.NotFoundUserException;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.addressrestservice.AddressRestService;
import com.usertest.service.userdtovalidationservice.UserDtoValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final String NOT_FOUND_USER_MESSAGE_PART_1 = "There is no user with ID = ";
    private static final String NOT_FOUND_USER_MESSAGE_PART_2 = " in database.";
    private static final String NOT_FOUND_NUMBER_MESSAGE_PART_1 = "There is no numbers with user ID = ";
    private static final String NOT_FOUND_NUMBER_MESSAGE_PART_2 = " in database.";

    private final AddressRestService addressRestService;

    private final UserRepository userRepository;

    private final NumberRepository numberRepository;

    private final UserMapper userMapper;

    private final UserDtoValidationService userDtoValidationService;

    @Override
    public UserDto getUserById(long id) throws ResourceAccessException, JsonProcessingException {
        UserEntity userEntity = userRepository.getUserById(id).orElseThrow(() ->
                new NotFoundUserException(NOT_FOUND_USER_MESSAGE_PART_1 + id + NOT_FOUND_USER_MESSAGE_PART_2));
        ResponseDto<AddressDto> responseAddressDto = addressRestService.getAddressById(userEntity.getAddressId());
        List<String> numberEntitiesList = numberRepository.getNumbersByUserId(id);
        return userMapper.toUserDto(userEntity, numberEntitiesList, responseAddressDto.getData());
    }

    @Override
    public List<UserDto> getUsersByFilters(String partOfName, String partOfNumber)
            throws ResourceAccessException, JsonProcessingException {
        List<UserWithNumberEntity> userEntities = userRepository.getUsersByFilters(partOfName, partOfNumber);
        HashMap<Long, UserDto> usersMap = new HashMap<>();
        for(UserWithNumberEntity entity : userEntities) {
            ResponseDto<AddressDto> addressDto = addressRestService.getAddressById(entity.getAddressId());
            if (usersMap.containsKey(entity.getId())) {
                UserDto userDto = usersMap.get(entity.getId());
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
    public UserDto saveUser(UserDto userDto) throws ResourceAccessException,JsonProcessingException {
        userDtoValidationService.validate(userDto);
        var addressDto = new AddressDto();
        addressDto.setAddress(userDto.getAddress());
        ResponseDto<AddressDto> responseAddressDto = addressRestService.findOrInsertAddress(addressDto);
        UserEntity userEntity = userRepository.saveUser(
                userMapper.toUserEntity(userDto, responseAddressDto.getData().getId())
        );
        numberRepository.saveNumbersList(userDto.getNumbers(), userEntity.getId());
        return userMapper.toUserDto(userEntity, userDto.getNumbers(), responseAddressDto.getData());
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) throws ResourceAccessException, JsonProcessingException {
        if (userDto.getId() == null || userId != userDto.getId()) {
            throw new IdValuesDoNotMatchException("User ids don't match. Id from url = " + userId + ". " +
                    "Id in user dto = " + userDto.getId());
        }
        userDtoValidationService.validate(userDto);
        var addressDto = new AddressDto();
        addressDto.setAddress(userDto.getAddress());
        userRepository.getUserById(userId).orElseThrow(() ->
                new NotFoundUserException(NOT_FOUND_USER_MESSAGE_PART_1 + userId + NOT_FOUND_USER_MESSAGE_PART_2));
        ResponseDto<AddressDto> addressResult = addressRestService.findOrInsertAddress(addressDto);

        UserEntity userEntity = userRepository.updateUser(
                userMapper.toUserEntity(userDto, addressResult.getData().getId())
        );
        List<String> existNumbers = numberRepository.getNumbersByUserId(userId);
        if(existNumbers != null && !existNumbers.isEmpty()) {
            int deleteNumbersResult = numberRepository.deleteNumbersByUserId(userId);
            if (deleteNumbersResult == 0) {
                throw new NotFoundNumberException(
                        NOT_FOUND_NUMBER_MESSAGE_PART_1 + userId + NOT_FOUND_NUMBER_MESSAGE_PART_2
                );
            }
        }
        if (userDto.getNumbers() != null && !userDto.getNumbers().isEmpty()) {
            numberRepository.saveNumbersList(userDto.getNumbers(), userId);
        }

        return userMapper.toUserDto(userEntity, userDto.getNumbers(), addressResult.getData());
    }

    @Transactional
    @Override
    public int deleteUserById(long userId) throws ResourceAccessException, JsonProcessingException {
        UserEntity userDto = userRepository.getUserById(userId).orElseThrow(() ->
                new NotFoundUserException(NOT_FOUND_USER_MESSAGE_PART_1 + userId + NOT_FOUND_USER_MESSAGE_PART_2));
        int result = userRepository.deleteUserById(userId);
        if (result == 0) {
            throw new NotFoundUserException(NOT_FOUND_USER_MESSAGE_PART_1 + userId + NOT_FOUND_USER_MESSAGE_PART_2);
        }
        List<String> existNumbers = numberRepository.getNumbersByUserId(userId);
        if (existNumbers != null && !existNumbers.isEmpty()) {
            int deleteNumbersResult = numberRepository.deleteNumbersByUserId(userId);
            if (deleteNumbersResult == 0) {
                throw new NotFoundNumberException(
                        NOT_FOUND_NUMBER_MESSAGE_PART_1 + userId + NOT_FOUND_NUMBER_MESSAGE_PART_2
                );
            }
        }
        Long addressId = userDto.getAddressId();
        if(addressId != null && userRepository.addressUsersCount(addressId) == 0) {
            addressRestService.deleteAddressById(addressId);
        }
        return result;
    }
}
