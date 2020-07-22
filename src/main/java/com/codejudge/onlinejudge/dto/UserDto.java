package com.codejudge.onlinejudge.dto;

import com.codejudge.onlinejudge.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {

    @NotBlank
    private String fullName;

    // TODO: Implement custom validators
    @ValidEmail
    @NotBlank
    private String email;

    // TODO: Implement custom validators
    @NotBlank
    @Size(min = 6)
    private String saltedHashedPassword;
}
