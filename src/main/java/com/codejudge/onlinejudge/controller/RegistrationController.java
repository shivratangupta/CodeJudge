package com.codejudge.onlinejudge.controller;

import com.codejudge.onlinejudge.dto.ResponseDto;
import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.dto.UserResponseDto;
import com.codejudge.onlinejudge.exception.InvalidVerificationTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseDto<UserResponseDto> registerUser(@Valid @RequestBody UserDto userDto,
                                                     HttpServletRequest request) throws UserAlreadyExistException {
        log.info("Received registration request for " + userDto.getEmail());
        User user = userService.registerUser(userDto, request);
        log.info("Registration request successfully completed");
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @GetMapping("/confirmRegistration")
    public ResponseDto<UserResponseDto> confirmRegistration(@RequestParam String token,
                                                            WebRequest webRequest) throws InvalidVerificationTokenException {
        log.info("Received registration confirmation request");
        User user = userService.confirmRegistration(token, webRequest);
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
