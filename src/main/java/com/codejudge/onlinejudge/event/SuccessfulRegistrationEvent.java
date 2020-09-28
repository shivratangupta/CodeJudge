package com.codejudge.onlinejudge.event;

import com.codejudge.onlinejudge.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class SuccessfulRegistrationEvent extends ApplicationEvent {

    private final User registeredUser;
    private String appUrl;
    private Locale locale;

    public SuccessfulRegistrationEvent(User registeredUser, Locale locale, String appUrl) {
        super(registeredUser);
        this.registeredUser = registeredUser;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
