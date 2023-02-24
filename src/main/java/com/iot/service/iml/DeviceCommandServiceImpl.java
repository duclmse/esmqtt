package com.iot.service.iml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.config.MqttConfig;
import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.msg.ServerMessage;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceCommandRepository;
import com.iot.service.interfaces.DeviceCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final DeviceCommandRepository repository;
    private final MqttClient publisher;
    private final MqttConfig config;

    @Override
    public Mono<Integer> sendControlMsg(ServerMessage msg) {
        var topic = String.format(config.dnTopic(), msg.devId());
        return Mono.fromCallable(() -> mapper.writeValueAsString(msg)).flatMap(s -> {
            try {
                publisher.publish(topic, s.getBytes(StandardCharsets.UTF_8), 1, false);
                return Mono.just(1);
            } catch (MqttException e) {
                return Mono.error(e);
            }
        });
    }

    @Override
    public Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req) {
        return Mono.justOrEmpty(repository.getCommandHistory(req));
    }
}
