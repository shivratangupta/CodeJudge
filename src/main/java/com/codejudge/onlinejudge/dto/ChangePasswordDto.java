package com.codejudge.onlinejudge.dto;

import com.codejudge.onlinejudge.validation.PasswordMatches;
import com.codejudge.onlinejudge.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordMatches
public class ChangePasswordDto {

    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    @ValidPassword
    private String newPassword;
    private String confirmNewPassword;
}
