package com.usertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.controller.initializer.MsSQLServer;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.userservice.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(initializers = MsSQLServer.Initializer.class)
class UserControllerTest {

    private static final String PATH = "/user/";
    private static final String PATH_WITH_ID = "/user/{id}";

    private static final String NAME = "Johnson";
    private static final String ADDRESS = "Canada";
    private static final List<String> NUMBERS = List.of("+1234567", "+1233845");
    private static final long ID = 1L;

    private static final String ADMIN = "admin";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String ADMIN_ENCRYPTED_PASSWORD = "{bcrypt}$2a$10$50Oag0ifCFghZ1pMU5WeSO1hKHfpgY2DHBAb2TUv/vgK7SWy81IqS";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeAll
    static void init() { MsSQLServer.container.start();}

    @Test
    @WithMockUser(username = ADMIN, password = ADMIN_ENCRYPTED_PASSWORD, roles = ADMIN_ROLE)
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
    @WithMockUser(username = ADMIN, password = ADMIN_ENCRYPTED_PASSWORD, roles = ADMIN_ROLE)
    void getUsersByFilters_shouldBeReturnedListOfUsersSuccessfully() throws Exception {
        //given
        var anotherId = 2L;
        var partOfName = "son";
        var partOfNumber = "12";

        var userDto = createUserDto();
        userDto.setId(ID);

        var anotherUserDto = UserDto.builder()
                .id(anotherId)
                .name("Peterson")
                .age(35)
                .numbers(List.of("+654321", "+123456"))
                .address("Mexico")
                .build();

        var userList = List.of(userDto, anotherUserDto);
        when(userService.getUsersByFilters(partOfName, partOfNumber)).thenReturn(userList);

        //when
        mockMvc.perform(
                get("/user/")
                        .queryParam("partOfName", partOfName)
                        .queryParam("partOfNumber", partOfNumber))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(userList))));

        //then
        verify(userService, times(1)).getUsersByFilters(partOfName, partOfNumber);
    }

    @Test
    @WithMockUser(username = ADMIN, password = ADMIN_ENCRYPTED_PASSWORD, roles = ADMIN_ROLE)
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
    @WithMockUser(username = ADMIN, password = ADMIN_ENCRYPTED_PASSWORD, roles = ADMIN_ROLE)
    void updateUser_shouldBeReturnedUserSuccessfully() throws Exception {
        //given
        var userDto = createUserDto();

        var resultUserDto = UserDto.builder()
                .id(ID)
                .name("Peterson")
                .age(35)
                .numbers(List.of("+654321", "+123456"))
                .address("Mexico")
                .build();

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
    @WithMockUser(username = ADMIN, password = ADMIN_ENCRYPTED_PASSWORD, roles = ADMIN_ROLE)
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
        return UserDto.builder()
                .name(NAME)
                .age(30)
                .numbers(NUMBERS)
                .address(ADDRESS)
                .build();
    }
}