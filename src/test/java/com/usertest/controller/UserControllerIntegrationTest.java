package com.usertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usertest.controller.initializer.MsSQLServer;
import com.usertest.dto.AddressDto;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.restservice.RestServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = MsSQLServer.Initializer.class)
class UserControllerIntegrationTest {

    private static final String PATH = "/user/";
    private static final String PATH_WITH_ID = "/user/{id}";
    private static final String NAME = "Johnson";
    private static final String ADDRESS = "Canada";
    private static final List<String> NUMBERS = List.of("+121111111");
    private static final int AGE = 25;
    private static final Long ID = 1L;

    @Value("${remote.url}")
    private String remoteUrl;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RestServiceImpl restService;

    @BeforeAll
    static void init() {
        MsSQLServer.container.start();
    }

    @AfterEach
    void clearDb() {
        deleteFromTables(jdbcTemplate, "number");
        deleteFromTables(jdbcTemplate, "[user]");
    }

    @Sql("/sql/test_data/save_one_user_on_db.sql")
    @Test
    void getUserById() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        var url = remoteUrl + "/address/1";
        var addressDto = new AddressDto(1L, ADDRESS);
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDto)));
        when(restService.sendGet(url)).thenReturn(responseEntityResult);

        //when
        var expectedUserDto = createUserDto();
        expectedUserDto.setId(ID);
        expectedUserDto.setAddress(ADDRESS);

        mockMvc.perform(
                        get(PATH_WITH_ID, ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(expectedUserDto))));

        //then
        verify(restService, times(1)).sendGet(url);
    }

    @Sql("/sql/test_data/get_users_by_filters_test_data.sql")
    @Test
    void getUsersByFilters() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(2);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(3);

        var userDto = createUserDto();
        userDto.setId(ID);

        var url = remoteUrl + "/address/1";
        var addressDto = new AddressDto(1L, ADDRESS);
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDto)));
        when(restService.sendGet(url)).thenReturn(responseEntityResult);

        //when
        var partOfName = "son";
        var partOfNumber = "12";
        mockMvc.perform(
                        get(PATH)
                                .queryParam("partOfName", partOfName)
                                .queryParam("partOfNumber", partOfNumber))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(List.of(userDto)))));

        //then
        verify(restService, times(1)).sendGet(url);
    }

    @Test
    void saveUser() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isZero();
        assertThat(countRowsInTable(jdbcTemplate, "number")).isZero();

        var userDto = createUserDto();

        var url = remoteUrl + "/address";
        var addressDto = new AddressDto(null, ADDRESS);
        var addressDtoResult = new AddressDto(1L, ADDRESS);
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDtoResult)));
        when(restService.sendPost(url, addressDto)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        post(PATH)
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Johnson"))
                .andExpect(jsonPath("$.data.age").value("25"))
                .andExpect(jsonPath("$.data.address").value("Canada"));

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        verify(restService, times(1)).sendPost(url, addressDto);
    }

    @Test
    void saveUser_checkTransactional() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isZero();
        assertThat(countRowsInTable(jdbcTemplate, "number")).isZero();

        var userDto = createUserDto();
        userDto.setNumbers(List.of("00000111112222233333444445555566666777778888899999123"));

        var url = remoteUrl + "/address";
        var addressDto = new AddressDto(null, ADDRESS);
        var addressDtoResult = new AddressDto(1L, ADDRESS);
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDtoResult)));
        when(restService.sendPost(url, addressDto)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        post(PATH)
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(0);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(0);

        verify(restService, times(1)).sendPost(url, addressDto);
    }

    @Sql("/sql/test_data/save_one_user_on_db.sql")
    @Test
    void updateUser() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        var updatedUserDto = UserDto.builder()
                .name(NAME)
                .age(30)
                .numbers(List.of("11111", "22222"))
                .address("London")
                .build();

        var updatedUserDtoWithId = UserDto.builder()
                .id(ID)
                .name(NAME)
                .age(30)
                .numbers(List.of("11111", "22222"))
                .address("London")
                .build();

        var url = remoteUrl + "/address";
        var addressDto = new AddressDto(null, "London");
        var addressDtoResult = new AddressDto(2L, "London");
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDtoResult)));
        when(restService.sendPost(url, addressDto)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        put(PATH_WITH_ID, ID)
                                .content(objectMapper.writeValueAsString(updatedUserDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(ResponseDto.okResponseDto(updatedUserDtoWithId))));

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(updatedUserDto.getNumbers().size());

        verify(restService, times(1)).sendPost(url, addressDto);
    }

    @Sql("/sql/test_data/save_one_user_on_db.sql")
    @Test
    void updateUser_checkTransactional() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        var updatedUserDto = UserDto.builder()
                .name(NAME)
                .age(30)
                .numbers(List.of("111111", "00000111112222233333444445555566666777778888899999123"))
                .address("London")
                .build();

        var url = remoteUrl + "/address";
        var addressDto = new AddressDto(null, "London");
        var addressDtoResult = new AddressDto(2L, "London");
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(addressDtoResult)));
        when(restService.sendPost(url, addressDto)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        put(PATH_WITH_ID, ID)
                                .content(objectMapper.writeValueAsString(updatedUserDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        verify(restService, times(1)).sendPost(url, addressDto);
    }

    @Sql("/sql/test_data/save_one_user_on_db.sql")
    @Test
    void deleteUserById() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        var url = remoteUrl + "/address/1";
        var responseEntityResult
                = ResponseEntity.ok(objectMapper.writeValueAsString(ResponseDto.okResponseDto(1L)));
        when(restService.sendDelete(url)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        delete(PATH_WITH_ID, ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("1"));

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isZero();
        assertThat(countRowsInTable(jdbcTemplate, "number")).isZero();

        verify(restService, times(1)).sendDelete(url);
    }

    @Sql("/sql/test_data/save_one_user_on_db.sql")
    @Test
    void deleteUserById_checkTransactional() throws Exception {
        //given
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        var url = remoteUrl + "/address/1";
        var responseEntityResult
                = new ResponseEntity(ResponseDto.failedResponseDto("exception message"), HttpStatus.BAD_REQUEST);
        when(restService.sendDelete(url)).thenReturn(responseEntityResult);

        //when
        mockMvc.perform(
                        delete(PATH_WITH_ID, ID))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.data").value("Incorrect response entity status on " +
                        "operation delete address by id. Received status: BAD_REQUEST"));

        //then
        assertThat(countRowsInTable(jdbcTemplate, "[user]")).isEqualTo(1);
        assertThat(countRowsInTable(jdbcTemplate, "number")).isEqualTo(1);

        verify(restService, times(1)).sendDelete(url);
    }


    private UserDto createUserDto() {
        return UserDto.builder()
                .name(NAME)
                .age(AGE)
                .numbers(NUMBERS)
                .address(ADDRESS)
                .build();
    }
}