package com.iot.config;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface IMqttPublisher {

    void sendToMqtt(String data);

}
