package com.iot.model.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
@Accessors(fluent = true)
public class DeviceMessageEvent extends ApplicationEvent {

    private final String deviceId;
    private final Instant ts;
    private final String msg;

    public DeviceMessageEvent(Object source, String deviceId, Instant ts, String msg) {
        super(source);
        this.deviceId = deviceId;
        this.ts = ts;
        this.msg = msg;
    }
}
