package com.iot.repository.interfaces;

import com.iot.model.CommandInfo;
import com.iot.model.request.CommandHistoryRequest;

import java.time.Instant;
import java.util.List;

public interface DeviceControlRepository {

    int saveCommand(String id, Instant ts, String command);

    List<CommandInfo> getCommandHistory(CommandHistoryRequest request);
}
