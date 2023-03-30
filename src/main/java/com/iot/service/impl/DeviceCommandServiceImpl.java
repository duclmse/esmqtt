package com.iot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.MqttConfig;
import com.iot.model.event.DeviceCommandEvent;
import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.msg.ServerMessage;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceCommandRepository;
import com.iot.repository.interfaces.DeviceRepository;
import com.iot.service.interfaces.DeviceCommandService;
import com.iot.service.interfaces.MqttPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ApplicationEventPublisher eventPublisher;
    private final DeviceRepository deviceRepository;
    private final DeviceCommandRepository repository;
    private final MqttPublisher publisher;
    private final MqttConfig config;


    @Override
    public Mono<Void> sendControlMsg(String deviceId, DeviceStatus status) {
        var topic = String.format(config.dnTopic(), deviceId);
        return Mono.fromCallable(() -> deviceRepository.readDevice(deviceId))
            .map(info -> ServerMessage.from(info).status(status))
            .flatMap(info -> {
                try {
                    return Mono.just(mapper.writeValueAsString(info));
                } catch (JsonProcessingException e) {
                    return Mono.error(e);
                }
            })
            .doOnNext(s -> eventPublisher.publishEvent(new DeviceCommandEvent(this, deviceId, s)))
            .flatMap(s -> publisher.publish(topic, s.getBytes(StandardCharsets.UTF_8), 1, false));
    }

    @Override
    public Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req) {
        return Mono.fromCallable(() -> repository.getCommandHistory(req));
    }
}
