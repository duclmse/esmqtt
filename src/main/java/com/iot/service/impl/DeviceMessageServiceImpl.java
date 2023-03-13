package com.iot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.MqttConfig;
import com.iot.model.event.DeviceMessageEvent;
import com.iot.model.msg.DeviceMessage;
import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.model.request.RestRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import com.iot.service.interfaces.DeviceMessageService;
import com.iot.service.interfaces.MqttPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceMessageServiceImpl implements MqttCallback, DeviceMessageService {

    private final WebClient.Builder webClientBuilder;
    private final ApplicationEventPublisher publisher;
    private final DeviceMessageRepository repository;
    private final MqttPublisher mqttPublisher;
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
        client.subscribe("rest/request");
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
            log.error("connection lost:", cause);
            this.subscriber.connect();
        } catch (MqttException e) {
            log.error("couldn't reconnect:", e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (Objects.equals(topic, "rest/request")) {
            handleRequest(message.getPayload());
            return;
        }
        var deviceId = topic.split("/")[3];
        var payload = new String(message.getPayload());
        var ts = Instant.now();
        log.info("messageArrived: topic {} -> msg length {}", topic, payload.length());
        publisher.publishEvent(new DeviceMessageEvent(this, deviceId, ts, payload));
        Mono.fromCallable(() -> mapper.readValue(payload, DeviceMessage.class))
            .map(msg -> repository.saveStatus(deviceId, ts, msg.status()))
            .subscribe();
    }

    private void handleRequest(byte[] payload) {
        try {
            var request = mapper.readValue(payload, RestRequest.class);
            log.info("request: {} {}", request.method(), request.endpoint());
            webClientBuilder.baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(request.method())
                .uri(request.endpoint())
                .headers(httpHeaders -> httpHeaders.putAll(request.headers()))
                .bodyValue(request.body())
                .exchangeToMono(res -> res.bodyToMono(Object.class))
                .flatMap(body -> {
                    try {
                        return mqttPublisher.publish("rest/response", mapper.writeValueAsBytes(body));
                    } catch (JsonProcessingException e) {
                        return Mono.empty();
                    }
                })
                .subscribe();
        } catch (Exception e) {
            log.error("error handling request", e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete {}", token);
    }
}
