package com.corenect.task.handlers;

import com.corenect.task.models.response.FailResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Map;

@RestControllerAdvice
public final class ControllerExceptionHandler {
    /**
     * 타입 에러 발생 시 에러 메시지 출력
     * @param e
     * @return
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String,?> getWrongTypeException(Exception e) {
        return new FailResponse.Builder(FailResponse.of.WRONG_PARAMS_EXCEPTION).build().getResponse();
    }
}
