package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(fluent = true)
public class MessageHistoryRequest {

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("from")
    private Instant from;

    @JsonProperty("to")
    private Instant to;

//    @JsonProperty("message")
//    private String message;

    @JsonProperty("limit")
    private Integer limit;

    @JsonProperty("offset")
    private Integer offset;
}