package com.iot.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iot.utils.HttpMethodDeserializer;
import com.iot.utils.HttpMethodSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;

import java.time.Instant;

@Data
@Accessors(fluent = true)
public class ApiHistory {
    @JsonProperty("ts")
    private Instant ts;

    @JsonProperty("method")
    @JsonSerialize(using = HttpMethodSerializer.class)
    @JsonDeserialize(using = HttpMethodDeserializer.class)
    private HttpMethod method;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("body")
    private String body;
}
