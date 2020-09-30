package com.codejudge.onlinejudge.service;

import com.codejudge.onlinejudge.dto.PasswordDto;
import com.codejudge.onlinejudge.dto.UserDto;
import com.codejudge.onlinejudge.event.SuccessfulRegistrationEvent;
import com.codejudge.onlinejudge.exception.InvalidTokenException;
import com.codejudge.onlinejudge.exception.UserAlreadyExistException;
import com.codejudge.onlinejudge.exception.UserNotFoundException;
import com.codejudge.onlinejudge.model.PasswordResetToken;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.model.VerificationToken;
import com.codejudge.onlinejudge.repository.PasswordResetTokenRepository;
import com.codejudge.onlinejudge.repository.UserRepository;
import com.codejudge.onlinejudge.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

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
    public User confirmRegistration(String token, WebRequest webRequest) throws InvalidTokenException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            log.info("Registration confirmation request is failed because token is invalid");
            throw new InvalidTokenException("Verification token " + token + " is invalid.");
        }

        if(verificationToken.getExpiryTime().getTime() - new Date().getTime() <= 0) {
            log.info("Registration confirmation request is failed because token has expired");
            throw new InvalidTokenException("Verification token " + token + " has expired.");
        }

        User verifiedUser = verificationToken.getUser();
        verifiedUser.setActive(true);
        userRepository.save(verifiedUser);

        log.info("Registration confirmation request is successful");
        return verifiedUser;
    }

    @Override
    public void resendVerificationToken(String existingToken, HttpServletRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(existingToken);
        verificationToken.updateToken();
        log.info("Verification token expiry time is updated");
        verificationTokenRepository.save(verificationToken);
        log.info("Saving verification token in the database");

        User user = verificationToken.getUser();
        String appUrl = "http://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath();
        String confirmationUrl = appUrl + "/confirmRegistration.html?token=" +
                verificationToken.getToken();
        String message = messageSource.getMessage("message.resendToken",
                null, request.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(message + "\r\n" + confirmationUrl);
        email.setFrom("support.email");
        email.setTo(user.getEmail());
        javaMailSender.send(email);
        log.info("verification email sent to the user " + user.getEmail());
    }

    @Override
    public void resetPassword(String email, HttpServletRequest request) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            log.info("Reset password request is failed because user " + email + " is not found!");
            throw new UserNotFoundException("user " + email + " not found!");
        }

        log.info("Creating new password reset token for user " + email);
        PasswordResetToken passwordResetToken = new PasswordResetToken(user);
        log.info("Saving password reset token to the database");
        passwordResetTokenRepository.save(passwordResetToken);

        String subject = "Reset Password";
        String confirmationUrl = request.getContextPath() + "/user/changePassword?token=" +
                passwordResetToken.getToken();
        String message = messageSource.getMessage("message.resetPassword",
                null, request.getLocale());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message + "\r\n" + confirmationUrl);
        simpleMailMessage.setTo(email);
        javaMailSender.send(simpleMailMessage);
        log.info("Reset password email sent to the user " + email);
    }

    @Override
    public void verifyPasswordToken(String token) throws InvalidTokenException {
        validatePasswordResetToken(token);
    }

    @Override
    public User savePassword(PasswordDto passwordDto) throws InvalidTokenException {
        PasswordResetToken passwordResetToken = validatePasswordResetToken(passwordDto.getToken());

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);

        return user;
    }

    private PasswordResetToken validatePasswordResetToken(String token) throws InvalidTokenException {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            throw new InvalidTokenException("Password reset token " + token + " is invalid");
        }

        if(passwordResetToken.getExpiryTime().getTime() - new Date().getTime() <= 0) {
            throw new InvalidTokenException("Password reset token " + token + " has expired.");
        }

        return passwordResetToken;
    }
}
