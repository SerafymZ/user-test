package com.usertest.service.userservice;

import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.entity.UserEntity;
import com.usertest.entity.UserWithNumberEntity;
import com.usertest.exception.NotFoundUserException;
import com.usertest.mapper.UserMapper;
import com.usertest.repository.NumberRepository;
import com.usertest.repository.UserRepository;
import com.usertest.service.addressrestservice.AddressRestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceImplTest {

    private static final String NAME = "Johnson";
    private static final String ADDRESS = "Canada";
    private static final String NUMBERS_LINE = "11111";
    private static final List<String> NUMBERS = List.of("11111");
    private static final int AGE = 25;
    private static final long ID = 1L;
    private static final long ADDRESS_ID = 101L;

    @Autowired
    UserServiceImpl userService;

    @MockBean
    AddressRestService addressRestService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    NumberRepository numberRepository;

    @MockBean
    UserMapper userMapper;

    @Test
    void getUserById_shouldBeThrewNotFoundUserException() {
        //given
        Mockito.when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> userService.getUserById(ID)).isInstanceOf(NotFoundUserException.class);
    }

    @Test
    void getUserById_shouldBeFindUserByIdSuccessful() {
        //given
        var userWithNumberEntity = createUserWithNumberEntity();
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.of(userWithNumberEntity));

        var addressDto = new AddressDto(ID, ADDRESS);
        when(addressRestService.getAddressById(ADDRESS_ID)).thenReturn(addressDto);

        var expectedResult = createUserDto();
        when(userMapper.toUserDto(userWithNumberEntity, NUMBERS, addressDto)).thenReturn(expectedResult);

        //when
        var actualResult = userService.getUserById(ID);

        //then
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(userRepository, times(1)).getUserWithNumbersById(ID);
        verify(addressRestService, times(1)).getAddressById(ADDRESS_ID);
        verify(userMapper, times(1)).toUserDto(userWithNumberEntity, NUMBERS, addressDto);
    }

    @Test
    void getUsersByFilters_getUsersSuccessful() {
        //given
        var partOfName = "son";
        var partOfNumber = "11";
        var userWithNumberEntity = createUserWithNumberEntity();
        when(userRepository.getUsersByFilters(partOfName, partOfNumber)).thenReturn(List.of(userWithNumberEntity));

        var addressDto = new AddressDto(ID, ADDRESS);
        when(addressRestService.getAddressById(ADDRESS_ID)).thenReturn(addressDto);

        var userDto = createUserDto();
        when(userMapper.toUserDto(userWithNumberEntity, NUMBERS, addressDto)).thenReturn(userDto);

        //when
        var actualResult = userService.getUsersByFilters(partOfName, partOfNumber);

        //then
        var expectedResult = List.of(userDto);
        assertThat(actualResult).isEqualTo(expectedResult);

        verify(userRepository, times(1)).getUsersByFilters(partOfName, partOfNumber);
        verify(addressRestService, times(1)).getAddressById(ADDRESS_ID);
        verify(userMapper, times(1)).toUserDto(userWithNumberEntity, NUMBERS, addressDto);
    }

    @Test
    void getUsersByFilters_shouldBeFindUserWithAddressIsNull() {
        //given
        var partOfName = "son";
        var partOfNumber = "11";
        var userWithNumberEntity = createUserWithNumberEntity();
        userWithNumberEntity.setAddressId(null);
        when(userRepository.getUsersByFilters(partOfName, partOfNumber)).thenReturn(List.of(userWithNumberEntity));
        when(addressRestService.getAddressById(0L)).thenReturn(null);

        var userDto = createUserDto();
        userDto.setAddress(null);
        when(userMapper.toUserDto(userWithNumberEntity, NUMBERS, null)).thenReturn(userDto);

        //when
        var actualResult = userService.getUsersByFilters(partOfName, partOfNumber);

        //then
        assertThat(actualResult).isEqualTo(List.of(userDto));

        verify(userRepository, times(1)).getUsersByFilters(partOfName, partOfNumber);
        verify(addressRestService, never()).getAddressById(0L);
        verify(userMapper, times(1)).toUserDto(userWithNumberEntity, NUMBERS, null);
    }

    @Test
    void getUsersByFilters_shouldBeSecondNumberAddedToFirstUserDtoNumbers() {
        //given
        var partOfName = "son";
        var partOfNumber = "11";
        var userWithNumberEntityFirst = createUserWithNumberEntity();
        var userWithNumberEntitySecond = createUserWithNumberEntity();
        userWithNumberEntitySecond.setNumber("22211");
        when(userRepository.getUsersByFilters(partOfName, partOfNumber))
                .thenReturn(List.of(userWithNumberEntityFirst, userWithNumberEntitySecond));

        var addressDto = new AddressDto(ID, ADDRESS);
        when(addressRestService.getAddressById(ADDRESS_ID)).thenReturn(addressDto);

        var userDtoFirst = createUserDto();
        userDtoFirst.setNumbers(new ArrayList<>(List.of("11111")));
        when(userMapper.toUserDto(userWithNumberEntityFirst, userDtoFirst.getNumbers(), addressDto))
                .thenReturn(userDtoFirst);
        var userDtoSecond = createUserDto();
        userDtoSecond.setNumbers(new ArrayList<>(List.of("22211")));
        when(userMapper.toUserDto(userWithNumberEntitySecond, userDtoSecond.getNumbers(), addressDto))
                .thenReturn(userDtoSecond);

        //then
        var actualResult = userService.getUsersByFilters(partOfName, partOfNumber);

        //then
        var resultUserDto = createUserDto();
        resultUserDto.setNumbers(new ArrayList<>(Arrays.asList("11111", "22211")));
        assertThat(actualResult).isEqualTo(List.of(resultUserDto));

        verify(userRepository, times(1)).getUsersByFilters(partOfName, partOfNumber);
        var iterationsCount = 2;
        verify(addressRestService, times(iterationsCount)).getAddressById(ADDRESS_ID);
        verify(userMapper, times(1)).toUserDto(userWithNumberEntityFirst, NUMBERS, addressDto);
        verify(userMapper, times(1))
                .toUserDto(userWithNumberEntitySecond, List.of("22211"), addressDto);
    }

    @Test
    void saveUser_shouldBeReturnedUserDtoSuccessfully() {
        //given
        var addressDtoWithoutId = new AddressDto(null, ADDRESS);
        var addressDto = new AddressDto(ADDRESS_ID, ADDRESS);
        when(addressRestService.findOrInsertAddress(addressDtoWithoutId)).thenReturn(addressDto);
        var userDto = createUserDto();
        var userEntity = createUserEntity();
        when(userMapper.toUserEntity(userDto, ADDRESS_ID)).thenReturn(userEntity);
        when(userRepository.saveUser(userEntity)).thenReturn(userEntity);
        int resultSaving = 1;
        when(numberRepository.saveNumbersList(NUMBERS, ID)).thenReturn(resultSaving);
        when(userMapper.toUserDto(userEntity, NUMBERS, addressDto)).thenReturn(userDto);

        //when
        var actualResult = userService.saveUser(userDto);

        //then
        assertThat(actualResult).isEqualTo(userDto);
        verify(addressRestService, times(1)).findOrInsertAddress(addressDtoWithoutId);
        verify(userMapper, times(1)).toUserEntity(userDto, ADDRESS_ID);
        verify(userRepository, times(1)).saveUser(userEntity);
        verify(numberRepository, times(1)).saveNumbersList(NUMBERS, ID);
        verify(userMapper, times(1)).toUserDto(userEntity, NUMBERS, addressDto);
    }

    @Test
    void updateUser_shouldBeThrewNotFoundUserException() {
        //given
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> userService.updateUser(ID, createUserDto()));

        //then
        verify(userRepository, times(1)).getUserWithNumbersById(ID);
    }

    @Test
    void updateUser_shouldBeReturnedUserDtoSuccessfully() {
        //given
        var userWithNumberEntity = createUserWithNumberEntity();
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.of(userWithNumberEntity));
        var addressDto = new AddressDto(ADDRESS_ID, ADDRESS);
        var addressDtoWithoutId = new AddressDto(null, ADDRESS);
        when(addressRestService.findOrInsertAddress(addressDtoWithoutId)).thenReturn(addressDto);
        var userDto = createUserDto();
        var userEntity = createUserEntity();
        when(userMapper.toUserEntity(userDto, ADDRESS_ID)).thenReturn(userEntity);
        when(userRepository.updateUser(userEntity)).thenReturn(userEntity);
        int result = 1;
        when(numberRepository.deleteNumbersByUserId(ID)).thenReturn(result);
        when(numberRepository.saveNumbersList(NUMBERS, ID)).thenReturn(result);
        when(userMapper.toUserDto(userEntity, NUMBERS, addressDto)).thenReturn(userDto);

        //when
        var actualResult = userService.updateUser(ID, userDto);

        //then
        assertThat(actualResult).isEqualTo(userDto);

        verify(userRepository, times(1)).getUserWithNumbersById(ID);
        verify(addressRestService, times(1)).findOrInsertAddress(addressDtoWithoutId);
        verify(userMapper, times(1)).toUserEntity(userDto, ADDRESS_ID);
        verify(userRepository, times(1)).updateUser(userEntity);
        verify(numberRepository, times(1)).deleteNumbersByUserId(ID);
        verify(numberRepository, times(1)).saveNumbersList(NUMBERS, ID);
        verify(userMapper, times(1)).toUserDto(userEntity, NUMBERS, addressDto);
    }

    @Test
    void deleteUserById_shouldBeThrewNotFoundUserException() {
        //given
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> userService.deleteUserById(ID)).isInstanceOf(NotFoundUserException.class);

        //then
        verify(userRepository, times(1)).getUserWithNumbersById(ID);
    }

    @Test
    void deleteUserById_shouldBeDeletedUserNumbersAddressSuccessful() {
        //given
        var userWithNumberEntity = createUserWithNumberEntity();
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.of(userWithNumberEntity));
        when(numberRepository.getNumbersByUserId(ID)).thenReturn(NUMBERS);
        int result = 1;
        when(numberRepository.deleteNumbersByUserId(ID)).thenReturn(result);
        when(userRepository.deleteUserById(ID)).thenReturn(result);
        int usersCount = 0;
        when(userRepository.addressUsersCount(ADDRESS_ID)).thenReturn(usersCount);
        when(addressRestService.deleteAddressById(ADDRESS_ID)).thenReturn(result);

        //when
        var actualResult = userService.deleteUserById(ID);

        //then
        assertThat(actualResult).isEqualTo(result);

        verify(userRepository, times(1)).getUserWithNumbersById(ID);
        verify(numberRepository, times(1)).getNumbersByUserId(ID);
        verify(numberRepository, times(1)).deleteNumbersByUserId(ID);
        verify(userRepository, times(1)).deleteUserById(ID);
        verify(userRepository, times(1)).addressUsersCount(ADDRESS_ID);
        verify(addressRestService, times(1)).deleteAddressById(ADDRESS_ID);
    }

    @Test
    void deleteUserById_shouldBeDeletedOnlyUser() {
        //given
        var userWithNumberEntity = createUserWithNumberEntity();
        when(userRepository.getUserWithNumbersById(ID)).thenReturn(Optional.of(userWithNumberEntity));
        when(numberRepository.getNumbersByUserId(ID)).thenReturn(null);
        int result = 1;
        when(numberRepository.deleteNumbersByUserId(ID)).thenReturn(result);
        when(userRepository.deleteUserById(ID)).thenReturn(result);
        int usersCount = 1;
        when(userRepository.addressUsersCount(ADDRESS_ID)).thenReturn(usersCount);
        when(addressRestService.deleteAddressById(ADDRESS_ID)).thenReturn(result);

        //when
        var actualResult = userService.deleteUserById(ID);

        //then
        assertThat(actualResult).isEqualTo(result);

        verify(userRepository, times(1)).getUserWithNumbersById(ID);
        verify(numberRepository, times(1)).getNumbersByUserId(ID);
        verify(numberRepository, never()).deleteNumbersByUserId(ID);
        verify(userRepository, times(1)).deleteUserById(ID);
        verify(userRepository, times(1)).addressUsersCount(ADDRESS_ID);
        verify(addressRestService, never()).deleteAddressById(ADDRESS_ID);
    }

    private UserWithNumberEntity createUserWithNumberEntity() {
        return UserWithNumberEntity.builder()
                .id(ID)
                .name(NAME)
                .age(AGE)
                .number(NUMBERS_LINE)
                .addressId(ADDRESS_ID)
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .id(ID)
                .name(NAME)
                .age(AGE)
                .numbers(NUMBERS)
                .address(ADDRESS)
                .build();
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(ID)
                .name(NAME)
                .age(AGE)
                .addressId(ADDRESS_ID)
                .build();
    }
}