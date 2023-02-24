package com.iot.service.iml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.MqttConfig;
import com.iot.model.event.DeviceMessageEvent;
import com.iot.model.msg.DeviceMessage;
import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import com.iot.service.interfaces.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceMessageServiceImpl implements MqttCallback, DeviceMessageService {

    private final ApplicationEventPublisher publisher;
    private final DeviceMessageRepository repository;
    private final MqttConfig config;

    private final ObjectMapper mapper = new ObjectMapper();

    private MqttClient subscriber = null;

    @Primary
    @Bean("subscriber")
    public MqttClient mqttSubscriberClient(MqttCallback handler) throws MqttException {
        var clientId = config.subClientId();
        if (clientId == null) {
            clientId = MqttClient.generateClientId();
        }
        var client = new MqttClient(config.url(), clientId, new MemoryPersistence());
        client.connect(config.connectOptions());
        client.subscribe(config.upTopic());
        client.setCallback(handler);
        log.info("subscribed to {} with client id {}", config.upTopic(), clientId);
        this.subscriber = client;
        return client;
    }

    @Override
    public Mono<List<DeviceMessageHistory>> getMessageHistory(MessageHistoryRequest req) {
        return Mono.justOrEmpty(repository.getMessages(req));
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            log.error("connection lost: {}", cause.getMessage());
            this.subscriber.connect();
        } catch (MqttException e) {
            log.error("couldn't reconnect {}", cause.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        var deviceId = topic.split("/")[3];
        var payload = new String(message.getPayload());
        var ts = Instant.now();
        log.info("messageArrived: topic {} -> msg length {}", topic, payload.length());
        publisher.publishEvent(new DeviceMessageEvent(this, deviceId, ts, payload));
        Mono.fromCallable(() -> mapper.readValue(payload, DeviceMessage.class))
            .map(msg -> repository.saveStatus(deviceId, ts, msg.status()))
            .subscribe();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete {}", token);
    }
}
