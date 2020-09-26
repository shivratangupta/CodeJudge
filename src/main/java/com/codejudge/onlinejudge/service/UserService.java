package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.model.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User verifyUser(String token);

    void resendVerificationToken(String email);

    void resetPassword(String email);

    void verifyPasswordToken(String token);

    User updatePassword(String newPassword);
}
