package com.codejudge.onlinejudge.exception;

import javax.validation.constraints.NotBlank;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(@NotBlank String s) {
    }
}
