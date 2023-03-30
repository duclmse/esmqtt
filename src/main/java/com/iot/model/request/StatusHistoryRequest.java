package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(fluent = true)
public class StatusHistoryRequest {

    @JsonIgnore
    private String deviceId;

    @JsonProperty("from")
    private Instant from;

    @JsonProperty("to")
    private Instant to;

    @JsonProperty("limit")
    private Integer limit = 100;

    @JsonProperty("offset")
    private Integer offset = 0;
}
