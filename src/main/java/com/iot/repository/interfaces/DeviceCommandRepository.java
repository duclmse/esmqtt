package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.request.CommandHistoryRequest;

import java.time.Instant;
import java.util.List;

public interface DeviceCommandRepository {

    int saveCommand(String id, Instant ts, String command);

    List<DeviceCommandHistory> getCommandHistory(CommandHistoryRequest request);
}
