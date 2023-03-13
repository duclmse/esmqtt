package com.iot.model.msg;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class ServerMessage extends DeviceInfo {

    @JsonProperty("dps")
    private DeviceStatus status;

    @JsonIgnore
    @JsonAnyGetter
    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Object value) {
        properties.put(key, value);
    }

    public static ServerMessage from(DeviceInfo info) {
        return (ServerMessage) new ServerMessage().id(info.id())
            .name(info.name())
            .ip(info.ip())
            .version(info.version())
            .hubId(info.hubId())
            .productId(info.productId())
            .deviceType(info.deviceType())
            .homeId(info.homeId());
    }
}
