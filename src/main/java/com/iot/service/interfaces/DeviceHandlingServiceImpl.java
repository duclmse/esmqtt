package com.iot.service.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.IMqttPublisher;
import com.iot.service.iml.DeviceHandlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceHandlingServiceImpl implements DeviceHandlingService {

//    private final
    private final ObjectMapper mapper = new ObjectMapper();
    private final IMqttPublisher publisher;

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            System.out.println(message.getHeaders());
            System.out.println(message.getPayload());
        };
    }

    @Override
    public void sendControlMsg() {
        publisher.sendToMqtt("test");
    }

}
