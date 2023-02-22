package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceMessage;
import com.iot.model.request.MessageHistoryRequest;

import java.time.Instant;
import java.util.List;

public interface DeviceMessageRepository {

    int saveMessage(String id, Instant ts, String msg);

    List<DeviceMessage> getMessages(MessageHistoryRequest req);
}
