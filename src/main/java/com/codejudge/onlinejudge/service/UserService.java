package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.model.User;

public interface UserService {

    User registerUser(UserDto userDto);

    User verifyUser(String token);

    User resendVerificationToken(String email);
}
