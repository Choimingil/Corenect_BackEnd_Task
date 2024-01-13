package com.corenect.task.validators;

import com.corenect.task.validators.annotations.ValidInt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public final class IntegerValidator implements ConstraintValidator<ValidInt,String> {
    /**
     * 0보다 큰 정수 검증
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regexPattern = "^[1-9]\\d*$";
        return Pattern.compile(regexPattern).matcher(value).matches();
    }
}
