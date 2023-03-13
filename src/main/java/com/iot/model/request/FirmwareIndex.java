package com.iot.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class FirmwareIndex {

    @JsonProperty("firmware_version")
    private String firmwareVersion;

    @JsonProperty("hardware_version")
    private String hardwareVersion;
}
