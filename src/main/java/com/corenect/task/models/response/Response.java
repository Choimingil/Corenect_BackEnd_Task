package com.corenect.task.models.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Response Builder Pattern
 */
public abstract class Response {
    private final Map<String,?> response;

    abstract static class Builder<T extends Builder<T>>{
        Map<String,Object> response = new HashMap<>();

        public T add(String key, Object val){
            response.put(key,val);
            return self();
        }

        abstract Response build();

        protected abstract T self();
    }

    Response(Builder<?> builder){
        response = new HashMap<>(builder.response);
    }

    public Map<String, ?> getResponse() {
        return response;
    }
}
