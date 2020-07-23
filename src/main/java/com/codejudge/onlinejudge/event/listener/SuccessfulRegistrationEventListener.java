package com.codejudge.onlinejudge.event.listener;

import com.codejudge.onlinejudge.event.SuccessfulRegistrationEvent;
import com.codejudge.onlinejudge.model.User;
import com.codejudge.onlinejudge.model.VerificationToken;
import com.codejudge.onlinejudge.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SuccessfulRegistrationEventListener implements ApplicationListener<SuccessfulRegistrationEvent> {

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Override
    public void onApplicationEvent(SuccessfulRegistrationEvent successfulRegistrationEvent) {

        User registeredUser = successfulRegistrationEvent.getRegisteredUser();

        VerificationToken token = new VerificationToken(registeredUser);
        verificationTokenRepository.save(token);

        // TODO: Send email to the user
    }
}
