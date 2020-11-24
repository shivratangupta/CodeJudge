package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.ChangePasswordDto;
import com.codejudge.onlinejudge.dto.PasswordDto;
import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.exception.InvalidOldPasswordException;
import com.codejudge.onlinejudge.exception.InvalidTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.exception.UserNotFoundException;
import com.codejudge.onlinejudge.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User registerUser(UserDto userDto, HttpServletRequest request) throws UserAlreadyExistException;

    User confirmRegistration(String token, WebRequest webRequest) throws InvalidTokenException;

    void resendVerificationToken(String existingToken, HttpServletRequest request);

    void resetPassword(String email, HttpServletRequest request) throws UserNotFoundException;

    void verifyPasswordToken(String token) throws InvalidTokenException;

    User savePassword(PasswordDto passwordDto) throws InvalidTokenException;

    void changeUserPassword(ChangePasswordDto changePasswordDto,
                            Authentication authentication) throws InvalidOldPasswordException;
}
