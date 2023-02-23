package com.iot.model.event;

import org.springframework.context.ApplicationEvent;

public class DeviceMessageEvent extends ApplicationEvent {

    public DeviceMessageEvent(Object source) {
        super(source);
    }
}
