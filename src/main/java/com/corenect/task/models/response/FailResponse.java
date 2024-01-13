package com.corenect.task.models.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public class FailResponse extends Response {
    @RequiredArgsConstructor
    @Getter
    public enum of{
        WRONG_PARAMS_EXCEPTION(400, "파라미터 타입이 잘못되었습니다.");

        private final int code;
        private final String message;
    }
    private FailResponse(Builder builder){
        super(builder);
        Map<String, ?> info = builder.response;
    }

    public static class Builder extends Response.Builder<Builder>{
        public Builder(int code, String message){
            this.response.put("isSuccess",false);
            this.response.put("code",code);
            this.response.put("message",message);
        }

        public Builder(of element){
            this.response.put("isSuccess",false);
            this.response.put("code",element.getCode());
            this.response.put("message",element.getMessage());
        }

        @Override
        public FailResponse build() {
            return new FailResponse(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}