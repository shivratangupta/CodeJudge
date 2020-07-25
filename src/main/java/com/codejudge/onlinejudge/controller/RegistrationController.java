package com.codejudge.onlinejudge.controller;

import com.codejudge.onlinejudge.dto.ResponseDto;
import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.dto.UserResponseDto;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@RequestBody UserDto userDto) {

        User user = userService.registerUser(userDto);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @GetMapping("/user/verify")
    public ResponseDto<UserResponseDto> verifyUser(@RequestParam String token) {

        User user = userService.verifyUser(token);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @GetMapping("/user/resendVerificationToken")
    public ResponseDto<UserResponseDto> resendVerificationToken(@RequestParam String email) {

        User user = userService.resendVerificationToken(email);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFullName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }
}
