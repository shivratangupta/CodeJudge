package com.codejudge.onlinejudge.controller;

import com.codejudge.onlinejudge.dto.ResponseDto;
import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.dto.UserResponseDto;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@Valid @RequestBody UserDto userDto) throws UserAlreadyExistException {
        log.info("Received registration request for " + userDto.getEmail());
        User user = userService.registerUser(userDto);
        log.info("Registration request successfully completed");
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @GetMapping("/user/verify")
    public ResponseDto<UserResponseDto> verifyUser(@RequestParam String token) {
        User user = userService.verifyUser(token);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @PostMapping("/user/resendVerificationToken")
    public void resendVerificationToken(@RequestParam String email) {
        userService.resendVerificationToken(email);
    }
    
    @PostMapping("/user/resetPassword")
    public void resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
    }

    @GetMapping("/user/verifyPasswordToken")
    public void verifyPasswordToken(@RequestParam String token) {
        userService.verifyPasswordToken(token);
    }

    @GetMapping("/user/updatePassword")
    public ResponseDto<UserResponseDto> updatePassword(@RequestParam String newPassword) {
        User user = userService.updatePassword(newPassword);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }
}
