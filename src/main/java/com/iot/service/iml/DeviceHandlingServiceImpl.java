package com.iot.service.iml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.IMqttPublisher;
import com.iot.config.MqttConfig;
import com.iot.model.msg.DeviceMessage;
import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import com.iot.service.interfaces.DeviceHandlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceHandlingServiceImpl implements DeviceHandlingService {

    private final ObjectMapper mapper = new ObjectMapper();


    private final DeviceMessageRepository repository;
    private final MqttConfig mqttConfig;

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                var headers = message.getHeaders();
                var topic = Objects.requireNonNull(headers.get("mqtt_receivedTopic")).toString();
                var ts = (long) headers.get("timestamp");
                var id = topic.split("/")[2];
                var payload = message.getPayload().toString();
                var msg = Mono.fromCallable(() -> mapper.readValue(payload, DeviceMessage.class));
                var instant = Instant.ofEpochMilli(ts);
                Mono.fromCallable(() -> repository.saveMessage(id, instant, payload))
                    .subscribe();
            } catch (Exception e) {
                log.error("error ", e);
            }
        };
    }

    @Override
    public Mono<List<DeviceMessageHistory>> getMessageHistory(MessageHistoryRequest req) {
        return Mono.justOrEmpty(repository.getMessages(req));
    }
}
