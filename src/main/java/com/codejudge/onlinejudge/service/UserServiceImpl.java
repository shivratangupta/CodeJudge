package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.event.SuccessfulRegistrationEvent;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.model.VerificationToken;
import com.codejudge.onlinejudge.repository.UserRepository;
import com.codejudge.onlinejudge.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public User registerUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()) != null) {
            // TODO: Throw an exception
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        applicationEventPublisher.publishEvent(
                new SuccessfulRegistrationEvent(savedUser)
        );

        return savedUser;
    }

    @Override
    public User verifyUser(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(!validateToken(verificationToken))
            return null;

        User verifiedUser = verificationToken.getUser();
        verifiedUser.setActive(true);
        userRepository.save(verifiedUser);

        return verifiedUser;
    }

    @Override
    public void resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            // TODO: Throw an exception
        }

        VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
        if(verificationToken == null) {
            verificationToken = new VerificationToken(user);
            verificationTokenRepository.save(verificationToken);
        } else {
            verificationToken.updateToken();
        }

        // TODO: Send an email
    }

    @Override
    public void resetPassword(String email) {
        resendVerificationToken(email);
    }

    @Override
    public void verifyPasswordToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(!validateToken(verificationToken))
            // TODO: Throw an exception
    }

    @Override
    public User updatePassword(String newPassword) {

    }

    private boolean validateToken(VerificationToken verificationToken) {
        if(verificationToken == null || verificationToken.getExpiryTime().getTime() - new Date().getTime() <= 0)
            return false;

        return true;
    }
}
