package com.iot.service.iml;

import com.iot.service.interfaces.MqttPublisher;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttPublisherImpl implements MqttPublisher {

    private final MqttClient publisher;

    public MqttPublisherImpl(@Qualifier("publisher") MqttClient client) {
        this.publisher = client;
    }

    @Override
    public void publish(String topic, byte[] payload) {
        try {
            publisher.publish(topic, new MqttMessage(payload));
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", payload.length, topic);
        }
    }

    @Override
    public void publish(String topic, MqttMessage message) {
        try {
            publisher.publish(topic, message);
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", message.getPayload().length, topic);
        }
    }

    @Override
    public void publish(String topic, byte[] payload, int qos, boolean retain) {
        try {
            publisher.publish(topic, payload, qos, retain);
        } catch (MqttException e) {
            log.error("Couldn't pub {} bytes to {}", payload.length, topic);
        }
    }
}
