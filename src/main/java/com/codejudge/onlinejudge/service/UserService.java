package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.exception.InvalidVerificationTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.model.User;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    User registerUser(UserDto userDto, HttpServletRequest request) throws UserAlreadyExistException;

    User confirmRegistration(String token, WebRequest webRequest) throws InvalidVerificationTokenException;

    void resendVerificationToken(String existingToken, HttpServletRequest request);

    void resetPassword(String email);

    void verifyPasswordToken(String token);

    User updatePassword(String newPassword);
}
