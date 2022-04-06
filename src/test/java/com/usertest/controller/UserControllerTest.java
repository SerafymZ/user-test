package com.usertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.userservice.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UserControllerTest {

    private static final String PATH = "/user/";
    private static final String PATH_WITH_ID = "/user/{id}";


    private static final String NAME = "Johnson";
    private static final String ADDRESS = "Canada";
    private static final List<String> NUMBERS = List.of("+1234567", "+1233845");
    private static final long ID = 1L;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void getUserById_shouldBeReturnedUserSuccessfully() throws Exception {
        //given
        var userDto = createUserDto();
        userDto.setId(ID);
        when(userService.getUserById(ID)).thenReturn(userDto);

        //when
        mockMvc.perform(
                get(PATH_WITH_ID, ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(userDto))));

        //then
        verify(userService, times(1)).getUserById(ID);
    }

    @Test
    void getUsersByFilters_shouldBeReturnedListOfUsersSuccessfully() throws Exception {
        //given
        var anotherId = 2L;
        var partOfName = "son";
        var partOfNumber = "12";

        var userDto = createUserDto();
        userDto.setId(ID);

        var anotherUserDto = new UserDto();
        userDto.setId(anotherId);
        userDto.setName("Peterson");
        userDto.setAge(35);
        userDto.setNumbers(List.of("+654321", "+123456"));
        userDto.setAddress("Mexico");

        var userList = List.of(userDto, anotherUserDto);
        when(userService.getUsersByFilters(partOfName, partOfNumber)).thenReturn(userList);

        //when
        mockMvc.perform(
                get("/user?partOfName=son&partOfNumber=12"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(userList))));

        //then
        verify(userService, times(1)).getUsersByFilters(partOfName, partOfNumber);
    }

    @Test
    void saveUser_shouldBeReturnedUserSuccessfully() throws Exception {
        //given
        var userDto = createUserDto();

        var resultUserDto = createUserDto();
        resultUserDto.setId(ID);

        when(userService.saveUser(userDto)).thenReturn(resultUserDto);

        //when
        mockMvc.perform(
                post(PATH)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(resultUserDto))));

        //then
        verify(userService, times(1)).saveUser(userDto);
    }

    @Test
    void updateUser_shouldBeReturnedUserSuccessfully() throws Exception {
        //given
        var userDto = createUserDto();

        var resultUserDto = new UserDto();
        userDto.setId(ID);
        userDto.setName("Peterson");
        userDto.setAge(35);
        userDto.setNumbers(List.of("+654321", "+123456"));
        userDto.setAddress("Mexico");

        when(userService.updateUser(ID, userDto)).thenReturn(resultUserDto);

        //when
        mockMvc.perform(put(PATH_WITH_ID, ID)
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(resultUserDto))));

        //then
        verify(userService, times(1)).updateUser(ID, userDto);
    }

    @Test
    void deleteUserById_shouldBeReturnedIntSuccessfully() throws Exception {
        //given
        var result = 1;
        when(userService.deleteUserById(ID)).thenReturn(result);

        //when
        mockMvc.perform(
                delete(PATH_WITH_ID, ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(result));

        //then
        verify(userService, times(1)).deleteUserById(ID);
    }

    private UserDto createUserDto() {
        var userDto = new UserDto();
        userDto.setName(NAME);
        userDto.setAge(30);
        userDto.setNumbers(NUMBERS);
        userDto.setAddress(ADDRESS);
        return userDto;
    }
}