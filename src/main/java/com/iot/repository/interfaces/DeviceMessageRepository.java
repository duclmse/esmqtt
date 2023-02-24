package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.request.MessageHistoryRequest;

import java.time.Instant;
import java.util.List;

public interface DeviceMessageRepository {

    int saveMessage(String id, Instant ts, String msg);

    int saveStatus(String id, Instant ts, DeviceStatus msg);

    List<DeviceMessageHistory> getMessages(MessageHistoryRequest req);
}
