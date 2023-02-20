package com.iot.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

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


    @Value("${iot.mqtt.topic}")
    private String topic;

    @Value("${iot.mqtt.timeout}")
    private int timeout;

    @Value("${iot.mqtt.auto-reconnect}")
    private boolean autoReconnect;

//    @Bean("mqtt-publisher-client")
//    public MqttClient mqttPublisherClient() throws MqttException {
//        var client = new MqttClient(url, clientId, new MemoryPersistence());
//        client.connect(connectOptions());
//        return client;
//    }

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

    @Bean
    @Qualifier("mqttInputChannel")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        var adapter = new MqttPahoMessageDrivenChannelAdapter(url, clientId, topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(connectOptions());
        return factory;
    }

    @Bean
    @Qualifier("mqttOutboundChannel")
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(this.topic);
        return messageHandler;
    }

    @Bean
    public IntegrationFlow mqttOutboundFlow() {
        return f -> f.handle(new MqttPahoMessageHandler(url, clientId));
    }


}
