package com.iot.service.iml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.msg.ServerMessage;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceCommandRepository;
import com.iot.service.interfaces.DeviceCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final DeviceCommandRepository repository;
    private final MqttClient publisher;

    @Override
    public Mono<Integer> sendControlMsg(ServerMessage msg) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(msg))
            .map(s -> s.length());
        // return null;
    }

    @Override
    public Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req) {
        return Mono.justOrEmpty(repository.getCommandHistory(req));
    }

}
