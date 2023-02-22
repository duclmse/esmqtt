package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.request.MessageHistoryRequest;

import java.time.Instant;
import java.util.List;

public interface DeviceMessageRepository {

    int saveMessage(String id, Instant ts, String msg);

    List<DeviceMessageHistory> getMessages(MessageHistoryRequest req);
}
