package com.codejudge.onlinejudge.event.listener;

import com.codejudge.onlinejudge.event.SuccessfulRegistrationEvent;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.model.VerificationToken;
import com.codejudge.onlinejudge.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SuccessfulRegistrationEventListener implements ApplicationListener<SuccessfulRegistrationEvent> {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(SuccessfulRegistrationEvent successfulRegistrationEvent) {
        User registeredUser = successfulRegistrationEvent.getRegisteredUser();
        log.info("Received verification email request for user " + registeredUser.getEmail());
        VerificationToken token = new VerificationToken(registeredUser);
        log.info("Verification token is created for user " + registeredUser.getEmail());
        verificationTokenRepository.save(token);
        log.info("Verification token saved to database");

        String recipientAddress = registeredUser.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = successfulRegistrationEvent.getAppUrl() +
                "/confirmRegistration.html?token=" + token;
        String message = messageSource.getMessage("message.regSucc",
                null, successfulRegistrationEvent.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        javaMailSender.send(email);
        log.info("Verification email is sent to the user " + recipientAddress);
    }
}
