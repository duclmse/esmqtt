package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class FirmwareInfo extends FirmwareIndex {

    @JsonProperty
    private String name;

    @JsonProperty
    private String url;

    @JsonProperty
    private String location;

    @JsonProperty
    private String hash;

    @JsonProperty
    private Instant updateTs;
}
