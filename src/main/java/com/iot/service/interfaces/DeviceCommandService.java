package com.iot.service.interfaces;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.request.CommandHistoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceCommandService {

    Mono<Void> sendControlMsg(String deviceId, DeviceStatus msg);

    Mono<List<DeviceCommandHistory>> getCommandHistory(CommandHistoryRequest req);
}
