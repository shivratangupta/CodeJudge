package com.codejudge.onlinejudge.validation;

import com.codejudge.onlinejudge.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

   @Override
   public void initialize(PasswordMatches constraintAnnotation) {
   }

   @Override
   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      UserDto userDto = (UserDto) obj;
      return userDto.getPassword().equals(userDto.getConfirmPassword());
   }
}
