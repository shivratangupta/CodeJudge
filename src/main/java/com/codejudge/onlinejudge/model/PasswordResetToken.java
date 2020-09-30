package com.codejudge.onlinejudge.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
public class PasswordResetToken extends Token {

    public PasswordResetToken() {
        super();
    }

    public PasswordResetToken(User user) {
        super(user);
    }
}
