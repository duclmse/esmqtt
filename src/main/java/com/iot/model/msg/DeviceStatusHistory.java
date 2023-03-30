package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceStatusHistory extends DeviceFullInfo {

    @JsonProperty("history")
    private List<DeviceStatus> history;

    public static DeviceStatusHistory from(DeviceFullInfo info) {
        var history = new DeviceStatusHistory();
        history.id(info.id())
            .name(info.name())
            .ip(info.ip())
            .version(info.version())
            .hubId(info.hubId())
            .productId(info.productId())
            .deviceType(info.deviceType())
            .homeId(info.homeId());
        history.heartbeat(info.heartbeat()).expectHeartbeat(info.expectHeartbeat());
        return history;
    }
}
