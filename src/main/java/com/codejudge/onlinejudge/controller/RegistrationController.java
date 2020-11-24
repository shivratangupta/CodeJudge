package com.codejudge.onlinejudge.controller;

import com.codejudge.onlinejudge.dto.*;
import com.codejudge.onlinejudge.exception.InvalidOldPasswordException;
import com.codejudge.onlinejudge.exception.InvalidTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.exception.UserNotFoundException;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
                                                            WebRequest webRequest) throws InvalidTokenException {
        log.info("Received registration confirmation request");
        User user = userService.confirmRegistration(token, webRequest);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @PostMapping("/user/resendVerificationToken")
    public ResponseDto<String> resendVerificationToken(@RequestParam String existingToken,
                                                       HttpServletRequest request) {
        log.info("Received resend verification token request");
        userService.resendVerificationToken(existingToken, request);
        return new ResponseDto<>("New Verification email has been sent.",
                HttpStatus.OK);
    }
    
    @PostMapping("/user/resetPassword")
    public ResponseDto<String> resetPassword(@RequestParam String email,
                                             HttpServletRequest request) throws UserNotFoundException {
        log.info("Received reset password request for " + email);
        userService.resetPassword(email, request);
        return new ResponseDto<>("Reset password email has been sent.",
                HttpStatus.OK);
    }

    @GetMapping("/user/changePassword")
    public void verifyPasswordToken(@RequestParam String token) throws InvalidTokenException {
        userService.verifyPasswordToken(token);
    }

    @PostMapping("/user/savePassword")
    public ResponseDto<UserResponseDto> savePassword(@Valid @RequestBody PasswordDto passwordDto) throws InvalidTokenException {
        User user = userService.savePassword(passwordDto);
        return new ResponseDto<>(
                new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isActive()),
                HttpStatus.OK);
    }

    @PostMapping("/user/updatePassword")
    public ResponseDto<String> changeUserPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                  Authentication authentication) throws InvalidOldPasswordException {
        userService.changeUserPassword(changePasswordDto, authentication);
        return new ResponseDto<>("Password has been updated successfully",
                HttpStatus.OK);
    }
}
