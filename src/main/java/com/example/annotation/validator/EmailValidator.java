package com.example.annotation.validator;

import com.example.annotation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    private Pattern pattern = null;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return isValidEmail(email);
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        Matcher matcher = this.pattern.matcher(email);
        return matcher.matches();
    }
}