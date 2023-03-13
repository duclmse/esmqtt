package com.iot.model.msg;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceMessage extends DeviceInfo {

    @JsonProperty("dps")
    private DeviceStatus status;

    @JsonIgnore
    @JsonAnyGetter
    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Object value) {
        properties.put(key, value);
    }
}
