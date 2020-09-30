package com.codejudge.onlinejudge.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
public class VerificationToken extends Token {

    public VerificationToken() {
        super();
    }

    public VerificationToken(User user) {
        super(user);
    }

    public void updateToken() {
        setToken(generateRandomUniqueToken());
        setExpiryTime(calculateExpiryTime());
    }
}
