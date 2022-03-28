package com.usertest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseDto<UserDto> getUserById(@PathVariable Long id)
            throws ResourceAccessException, JsonProcessingException {
        var userResult = userService.getUserById(id);
        return new ResponseDto<>(userResult);
    }

    @GetMapping
    public ResponseDto<List<UserDto>> getUsersByFilters(
            @RequestParam(name = "partOfName") String partOfName,
            @RequestParam(name = "partOfNumber") String partOfNumber
    ) throws ResourceAccessException, JsonProcessingException {
        var users = userService.getUsersByFilters(partOfName, partOfNumber);
        return new ResponseDto<>(users);
    }

    @PostMapping
    public ResponseDto<UserDto> saveUser(@RequestBody UserDto user)
            throws ResourceAccessException, JsonProcessingException {
        var userResult = userService.saveUser(user);
        return new ResponseDto<>(userResult);
    }

    @PutMapping("/{id}")
    public ResponseDto<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto)
            throws ResourceAccessException, JsonProcessingException {
        var userResult = userService.updateUser(id, userDto);
        userResult.setNumbers(userDto.getNumbers());
        return new ResponseDto<>(userResult);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> deleteUserById(@PathVariable Long id)
            throws ResourceAccessException, JsonProcessingException {
        var result = userService.deleteUserById(id);
        return new ResponseDto<>(result);
    }
}
