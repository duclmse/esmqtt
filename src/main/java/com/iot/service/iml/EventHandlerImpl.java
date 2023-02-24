package com.iot.service.iml;

import com.iot.model.event.ApiCallEvent;
import com.iot.model.event.DeviceCommandEvent;
import com.iot.model.event.DeviceMessageEvent;
import com.iot.repository.interfaces.ApiHistoryRepository;
import com.iot.repository.interfaces.DeviceCommandRepository;
import com.iot.repository.interfaces.DeviceMessageRepository;
import com.iot.service.interfaces.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

    private final DeviceCommandRepository commandRepository;
    private final DeviceMessageRepository messageRepository;
    private final ApiHistoryRepository apiRepository;

    @Async
    @Override
    @EventListener
    public void apiCallEventListener(ApiCallEvent event) {
        log.info("handle api call event {}", event);

        Mono.fromCallable(() -> apiRepository.save(event.getTimestamp(), event.method(), event.url(), event.body()))
            .doOnNext(saved -> log.info("saved {} api call(s) to DB", saved))
            .subscribe();
    }

    @Async
    @Override
    @EventListener
    public void deviceMessageEventListener(DeviceMessageEvent event) {
        log.info("handle device message event {}", event);

        Mono.fromCallable(() -> messageRepository.saveMessage(event.deviceId(), event.ts(), event.msg()))
            .doOnNext(saved -> log.info("saved {} device message(s) to DB", saved))
            .subscribe();
    }

    @Async
    @Override
    @EventListener
    public void deviceMessageEventListener(DeviceCommandEvent event) {
        log.info("handle device message event {}", event);

        Mono.fromCallable(
            () -> commandRepository.saveCommand(event.deviceId(), Instant.ofEpochMilli(event.getTimestamp()),
                event.msg())).doOnNext(saved -> log.info("saved {} api call(s) to DB", saved)).subscribe();
    }
}
