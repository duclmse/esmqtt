package com.iot.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${iot.mqtt.url}")
    private String url;

    @Value("${iot.mqtt.publisher-client-id}")
    private String clientId;

    @Value("${iot.mqtt.username}")
    private String username;

    @Value("${iot.mqtt.password}")
    private String password;

    @Value("${iot.mqtt.timeout}")
    private int timeout;

    @Bean("mqtt-publisher-client")
    public MqttClient mqttPublisherClient() throws MqttException {
        var client = new MqttClient(url, clientId, new MemoryPersistence());
        client.connect(connectOptions());
        return client;
    }

    public MqttConnectOptions connectOptions() {
        var options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        return options;
    }
}
