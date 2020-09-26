package com.codejudge.onlinejudge.dto;

import com.codejudge.onlinejudge.validation.PasswordMatches;
import com.codejudge.onlinejudge.validation.ValidEmail;
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

    // TODO: Implement custom validators
    @NotBlank
    @Size(min = 6)
    private String password;
    private String confirmPassword;
}
