package com.iot.repository.interfaces;

import com.iot.model.CommandInfo;

public interface DeviceControlRepository {
    int saveCommand(CommandInfo info);
}
