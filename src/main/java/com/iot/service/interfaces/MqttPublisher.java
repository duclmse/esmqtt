package com.iot.service.interfaces;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import reactor.core.publisher.Mono;

public interface MqttPublisher {

    Mono<Void> publish(String topic, byte[] payload);

    Mono<Void> publish(String topic, MqttMessage message);

    Mono<Void> publish(String topic, byte[] payload, int qos, boolean retain);
}
