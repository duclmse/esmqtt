package com.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class CommandInfo {
    @JsonProperty("ts")
    private Instant ts;
}
