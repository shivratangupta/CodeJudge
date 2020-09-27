package com.codejudge.onlinejudge.exception;

import javax.validation.constraints.NotBlank;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(@NotBlank String message) {
        super(message);
    }
}
