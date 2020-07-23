package com.codejudge.onlinejudge.event;

import com.codejudge.onlinejudge.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SuccessfulRegistrationEvent extends ApplicationEvent {

    private final User registeredUser;

    public SuccessfulRegistrationEvent(User registeredUser) {
        super(registeredUser);
        this.registeredUser = registeredUser;
    }
}
