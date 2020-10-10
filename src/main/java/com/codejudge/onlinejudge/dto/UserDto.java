package com.codejudge.onlinejudge.dto;

import com.codejudge.onlinejudge.validation.PasswordMatches;
import com.codejudge.onlinejudge.validation.ValidEmail;
import com.codejudge.onlinejudge.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatches
public class UserDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @ValidEmail
    @NotBlank
    private String email;

    @ValidPassword
    @NotBlank
    private String password;
    private String confirmPassword;
}
