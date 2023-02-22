package com.iot.service.interfaces;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.request.CommandHistoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceCommandService {

    void sendControlMsg(String id, String msg);

    Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req);
}
