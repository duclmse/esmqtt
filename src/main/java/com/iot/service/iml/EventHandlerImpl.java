package com.iot.service.iml;

import com.iot.model.event.ApiCallEvent;
import com.iot.model.event.DeviceMessageEvent;
import com.iot.service.interfaces.DeviceCommandService;
import com.iot.service.interfaces.DeviceMessageService;
import com.iot.service.interfaces.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

    private final DeviceCommandService commandService;
    private final DeviceMessageService messageService;

    @Async
    @EventListener
    public void apiCallEventListener(ApiCallEvent event) {
        log.info("handle api call event {}", event);
    }

    @Async
    @EventListener
    public void deviceMessageEventListener(DeviceMessageEvent event) {
        log.info("handle device message event {}", event);
    }
}
