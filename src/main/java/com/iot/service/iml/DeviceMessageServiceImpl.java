package com.iot.service.iml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.MqttConfig;
import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import com.iot.service.interfaces.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceMessageServiceImpl implements MqttCallback, DeviceMessageService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final DeviceMessageRepository repository;

    @Override
    public Mono<List<DeviceMessageHistory>> getMessageHistory(MessageHistoryRequest req) {
        return Mono.justOrEmpty(repository.getMessages(req));
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("connectionLost {}", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("messageArrived: topic {} -> msg length {}", topic, message.getPayload().length);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete {}", token);
    }
}
