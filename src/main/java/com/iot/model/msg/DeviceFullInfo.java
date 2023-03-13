package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceFullInfo extends DeviceMessage {

    @JsonProperty("heartbeat")
    private Instant heartbeat;

    @JsonProperty("expect_hb")
    private Instant expectHeartbeat;
}
