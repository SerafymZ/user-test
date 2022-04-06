package com.usertest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseDto<UserDto> getUserById(@PathVariable @Min(1) Long id)
            throws ResourceAccessException, JsonProcessingException {
        UserDto userResult = userService.getUserById(id);
        return new ResponseDto<>(userResult);
    }

    @GetMapping
    public ResponseDto<List<UserDto>> getUsersByFilters(
            @RequestParam(name = "partOfName") @Size(max = 50) String partOfName,
            @RequestParam(name = "partOfNumber") @Size(max = 50) String partOfNumber
    ) {
        List<UserDto> users = userService.getUsersByFilters(partOfName, partOfNumber);
        return new ResponseDto<>(users);
    }

    @PostMapping
    public ResponseDto<UserDto> saveUser(@Valid @RequestBody UserDto user) {
        UserDto userResult = userService.saveUser(user);
        return new ResponseDto<>(userResult);
    }

    @PutMapping("/{id}")
    public ResponseDto<UserDto> updateUser(@PathVariable @Min(1) Long id, @Valid @RequestBody UserDto userDto) {
        UserDto userResult = userService.updateUser(id, userDto);
        userResult.setNumbers(userDto.getNumbers());
        return new ResponseDto<>(userResult);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> deleteUserById(@PathVariable @Min(1) Long id) {
        int result = userService.deleteUserById(id);
        return new ResponseDto<>(result);
    }
}
