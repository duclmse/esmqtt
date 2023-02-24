package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(fluent = true)
public class DeviceMessageHistory {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ts")
    private Instant ts;

    @JsonProperty("msg")
    private String msg;
}
