package com.iot.service.interfaces;

import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.MessageHistoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceHandlingService {

    Mono<List<DeviceMessageHistory>> getMessageHistory(MessageHistoryRequest req);
}
