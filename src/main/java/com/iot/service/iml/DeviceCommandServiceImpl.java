package com.iot.service.iml;

import com.iot.config.IMqttPublisher;
import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceCommandRepository;
import com.iot.service.interfaces.DeviceCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceCommandRepository repository;
    private final IMqttPublisher publisher;

    @Override
    public void sendControlMsg(String id, String msg) {
        publisher.sendToMqtt("test");
    }

    @Override
    public Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req) {
        return Mono.justOrEmpty(repository.getCommandHistory(req));
    }

}
