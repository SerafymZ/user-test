package com.usertest.controller;

import com.usertest.dto.UserDto;
import com.usertest.dto.basedto.ResponseDto;
import com.usertest.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseDto<UserDto> getUserById(@PathVariable long id) {
        var userResult = userService.getUserById(id);
        return new ResponseDto<>(userResult);
    }

    @GetMapping("")
    public ResponseDto<List<UserDto>> getUsersByFilters(
            @RequestParam(name = "partOfName") String partOfName,
            @RequestParam(name = "partOfNumber") String partOfNumber
    ) {
        var users = userService.getUsersByFilters(partOfName, partOfNumber);
        return new ResponseDto<>(users);
    }

    @PostMapping()
    public ResponseDto<UserDto> saveUser(@RequestBody UserDto user) {
        var userResult = userService.saveUser(user);
        return new ResponseDto<>(userResult);
    }

    @PutMapping()
    public ResponseDto<UserDto> updateUser(@RequestBody UserDto userDto) {
        var userResult = userService.updateUser(userDto);
        userResult.setNumbers(userDto.getNumbers());
        return new ResponseDto<>(userResult);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> deleteUserById(@PathVariable long id) {
        var result = userService.deleteUserById(id);
        return new ResponseDto<>(result);
    }
}
