package com.corenect.task.models.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Success시 리턴하는 Response 출력
 */
public class SuccessResponse extends Response {
    @RequiredArgsConstructor
    @Getter
    public enum of{
        GET_SUCCESS(200,"성공적으로 조회되었습니다.");

        private final int code;
        private final String message;
    }
    private SuccessResponse(Builder builder){
        super(builder);
        Map<String, ?> info = builder.response;
    }

    public static class Builder extends Response.Builder<Builder>{
        public Builder(of element){
            this.response.put("isSuccess",true);
            this.response.put("code",element.getCode());
            this.response.put("message",element.getMessage());
        }

        @Override
        public SuccessResponse build() {
            return new SuccessResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
