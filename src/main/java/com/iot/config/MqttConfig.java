package com.iot.config;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Accessors(fluent = true)
@Configuration
public class MqttConfig {

    @Value("${iot.mqtt.url}")
    private String url;

    @Value("${iot.mqtt.pub-client-id}")
    private String pubClientId = null;

    @Value("${iot.mqtt.sub-client-id}")
    private String subClientId = null;

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

    @Bean("publisher")
    public MqttClient mqttPublisherClient() throws MqttException {
        var clientId = this.pubClientId;
        if (clientId == null || clientId.equals("")) {
            clientId = MqttClient.generateClientId();
        }
        var client = new MqttClient(this.url, clientId, new MemoryPersistence());
        client.connect(connectOptions());
        log.info("publisher {} connected to the broker @ {}", clientId, this.url);
        return client;
    }

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
}
