package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public User registerUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()) != null) {
            // TODO: Throw an exception
        }

        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setActive(false);
        user.setSaltedHashedPassword(passwordEncoder.encode(userDto.getSaltedHashedPassword()));

        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
