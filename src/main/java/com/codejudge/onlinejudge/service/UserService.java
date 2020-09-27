package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.model.User;

public interface UserService {

    User registerUser(UserDto userDto) throws UserAlreadyExistException;

    User verifyUser(String token);

    void resendVerificationToken(String email);

    void resetPassword(String email);

    void verifyPasswordToken(String token);

    User updatePassword(String newPassword);
}
