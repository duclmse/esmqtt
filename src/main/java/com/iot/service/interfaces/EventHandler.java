package com.iot.service.interfaces;

import com.iot.model.event.ApiCallEvent;
import com.iot.model.event.DeviceCommandEvent;
import com.iot.model.event.DeviceMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface EventHandler {
    @Async
    @EventListener
    void apiCallEventListener(ApiCallEvent event);

    @Async
    @EventListener
    void deviceMessageEventListener(DeviceMessageEvent event);

    @Async
    @EventListener
    void deviceMessageEventListener(DeviceCommandEvent event);
}
