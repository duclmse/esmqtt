package com.iot.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MqttConfig {

    @Value("${iot.mqtt.url}")
    private String url;

    @Value("${iot.mqtt.client-id}")
    private String clientId;

    @Value("${iot.mqtt.username}")
    private String username;

    @Value("${iot.mqtt.password}")
    private String password;

    @Value("${iot.mqtt.qos}")
    private int qos;

    @Value("${iot.mqtt.up-topic}")
    private String upTopic;

    @Value("${iot.mqtt.dn-topic}")
    private String dnTopic;

    @Value("${iot.mqtt.timeout}")
    private int timeout;

    @Value("${iot.mqtt.auto-reconnect}")
    private boolean autoReconnect;

    public MqttConnectOptions connectOptions() {
        var options = new MqttConnectOptions();
        options.setServerURIs(new String[]{this.url});
        options.setCleanSession(true);
        options.setUserName(this.username);
        options.setPassword(this.password.toCharArray());
        options.setConnectionTimeout(this.timeout);
        options.setAutomaticReconnect(this.autoReconnect);
        return options;
    }

    @Bean("mqtt-publisher-client")
    public MqttClient mqttPublisherClient() throws MqttException {
        var client = new MqttClient(url, clientId, new MemoryPersistence());
        client.connect(connectOptions());
        return client;
    }

    @Primary
    @Bean("mqtt-subscriber-client")
    public MqttClient mqttSubscriberClient(MqttCallback handler) throws MqttException {
        var client = new MqttClient(url, clientId, new MemoryPersistence());
        client.connect(connectOptions());
        client.setCallback(handler);
        return client;
    }
}
