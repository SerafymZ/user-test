package com.usertest.service.userdtovalidationservice;

import com.usertest.dto.UserDto;
import com.usertest.exception.IncorrectAgeException;
import com.usertest.exception.MaxStringFieldValueExceedException;
import org.springframework.stereotype.Service;

@Service
public class UserDtoValidationServiceImpl implements UserDtoValidationService{

    private static final int MAX_VARCHAR_LENGTH = 50;
    private static final int MAX_AGE = 130;

    @Override
    public void validate(UserDto userDto) {
        if (userDto == null) {
            throw new NullPointerException("User dto is NULL.");
        }

        stringValidate(userDto.getName(), "name");
        ageValidate(userDto.getAge());
        stringValidate(userDto.getAddress(), "address");
        if(userDto.getNumbers() != null) {
            for (String number : userDto.getNumbers()) {
                stringValidate(number, "number");
            }
        }
    }

    private void stringValidate(String value, String fieldName) {
        if (value == null) return;

        if(value.length() > MAX_VARCHAR_LENGTH) {
            throw new MaxStringFieldValueExceedException("Exceeded maximum value for string field "
                    + fieldName + ". " + " Value: " + value + ". " + "Value length = " +value.length());
        }
    }

    private void ageValidate(int age) {
        if (age < 0 || age > MAX_AGE) {
            throw new IncorrectAgeException("Incorrect age value. User age is " + age + " years.");
        }
    }
}
