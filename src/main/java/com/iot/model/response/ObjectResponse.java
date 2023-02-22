package com.iot.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectResponse {

    @JsonProperty
    private final int code;

    @JsonProperty
    private final String message;

    @JsonProperty
    private Object data;

    @JsonProperty
    private Object error;

    public ObjectResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ObjectResponse of(int code, String message) {
        return new ObjectResponse(code, message);
    }

    public static ObjectResponse of(int code, String message, Object data) {
        return new ObjectResponse(code, message, data);
    }

    public static ObjectResponse error(int code, String message, Object error) {
        return new ObjectResponse(code, message, null, error);
    }
}
