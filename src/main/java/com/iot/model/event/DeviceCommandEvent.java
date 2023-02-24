package com.iot.model.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true)
public class DeviceCommandEvent extends ApplicationEvent {

    private final String deviceId;
    private final String msg;

    public DeviceCommandEvent(Object source, String deviceId, String msg) {
        super(source);
        this.deviceId = deviceId;
        this.msg = msg;
    }
}
