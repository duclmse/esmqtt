package com.iot.service.impl;

import com.iot.service.interfaces.MqttPublisher;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MqttPublisherImpl implements MqttPublisher {

    private final MqttClient publisher;

    public MqttPublisherImpl(@Qualifier("publisher") MqttClient client) {
        this.publisher = client;
    }

    @Override
    public Mono<Void> publish(String topic, byte[] payload) {
        try {
            log.info("published to topic {}, msg: {}", topic, new String(payload));
            publisher.publish(topic, new MqttMessage(payload));
            return Mono.empty().then();
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", payload.length, topic);
            return Mono.empty();
        }
    }

    @Override
    public Mono<Void> publish(String topic, MqttMessage message) {
        try {
            log.info("published to topic {}, msg: {}", topic, message.getPayload());
            publisher.publish(topic, message);
            return Mono.empty().then();
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", message.getPayload().length, topic);
            return Mono.empty();
        }
    }

    @Override
    public Mono<Void> publish(String topic, byte[] payload, int qos, boolean retain) {
        try {
            publisher.publish(topic, payload, qos, retain);
            log.info("published to topic {}, msg: {}", topic, new String(payload));
            return Mono.empty().then();
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", payload.length, topic);
            return Mono.empty();
        }
    }
}
