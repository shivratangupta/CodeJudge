package com.codejudge.onlinejudge.dto;

import com.codejudge.onlinejudge.validation.PasswordMatches;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatches
public class PasswordDto {

    private String token;

    // TODO: Implement custom validators
    @NotBlank
    @Size(min = 6)
    private String newPassword;
    private String confirmNewPassword;
}
