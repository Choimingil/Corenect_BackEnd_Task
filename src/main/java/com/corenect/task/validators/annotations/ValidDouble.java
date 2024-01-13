package com.corenect.task.validators.annotations;

import com.corenect.task.validators.DoubleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DoubleValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDouble {
    String message() default "유효한 double 타입이 아닙니다.";
    boolean required() default true; // required = false parameter를 위한 처리, 이 값이 true면 param value가 null일 경우 예외 리턴, false일 경우 value가 null일 경우 패스
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
