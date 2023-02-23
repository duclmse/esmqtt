package com.iot.service.interfaces;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.msg.ServerMessage;
import com.iot.model.request.CommandHistoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceCommandService {

    Mono<Integer> sendControlMsg(ServerMessage msg);

    Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req);
}
