package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.event.SuccessfulRegistrationEvent;
import com.codejudge.onlinejudge.exception.InvalidVerificationTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.model.VerificationToken;
import com.codejudge.onlinejudge.repository.UserRepository;
import com.codejudge.onlinejudge.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto, HttpServletRequest request) throws UserAlreadyExistException {
        if(userRepository.findByEmail(userDto.getEmail()) != null) {
            log.info("Registration Request failed because account is already exist with email address: " + userDto.getEmail());
            throw new UserAlreadyExistException("There is an account with that email address: "
            + userDto.getEmail());
        }

        log.info("Creating new user with email address: " + userDto.getEmail());
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        log.info("Saving new user into database");
        User savedUser = userRepository.save(user);
        log.info("Saved new user into database");

        try {
            applicationEventPublisher.publishEvent(
                    new SuccessfulRegistrationEvent(savedUser, request.getLocale(), request.getContextPath())
            );
        } catch (RuntimeException ex) {
            // TODO: throw an exception
        }

        log.info("registration request is successful");
        return savedUser;
    }

    @Override
    public User confirmRegistration(String token, WebRequest webRequest) throws InvalidVerificationTokenException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            log.info("Registration confirmation request is failed because token is invalid");
            throw new InvalidVerificationTokenException("Token " + token + " is invalid.");
        }

        if(verificationToken.getExpiryTime().getTime() - new Date().getTime() <= 0) {
            log.info("Registration confirmation request is failed because token has expired");
            throw new InvalidVerificationTokenException("Token " + token + " has expired.");
        }

        User verifiedUser = verificationToken.getUser();
        verifiedUser.setActive(true);
        userRepository.save(verifiedUser);

        log.info("Registration confirmation request is successful");
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
        //if(!validateToken(verificationToken))
            // TODO: Throw an exception
    }

    @Override
    public User updatePassword(String newPassword) {
        return null;
    }

    private boolean validateToken(VerificationToken verificationToken) {
        if(verificationToken == null || verificationToken.getExpiryTime().getTime() - new Date().getTime() <= 0)
            return false;

        return true;
    }
}
