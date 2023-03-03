package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iot.utils.HttpMethodDeserializer;
import com.iot.utils.HttpMethodSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class RestRequest {

    @JsonProperty
    private String endpoint;

    @JsonProperty
    @JsonSerialize(using = HttpMethodSerializer.class)
    @JsonDeserialize(using = HttpMethodDeserializer.class)
    private HttpMethod method;

    @JsonProperty
    private Map<String, List<String>> headers = new HashMap<>();

    @JsonProperty
    private Object body = "";
}
