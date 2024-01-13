package com.corenect.task.validators;

import com.corenect.task.validators.annotations.ValidDouble;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public final class DoubleValidator implements ConstraintValidator<ValidDouble,String> {
    private boolean required; // ValidDouble 어노테이션에 존재하는 required 옵션

    /**
     * 0보다 큰 소수 검증
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required && value == null) return false;
        if(!required && value == null) return true;

        String regexPattern = "^[1-9]\\d*(\\.\\d+)?$";
        return Pattern.compile(regexPattern).matcher(value).matches();
    }
}
