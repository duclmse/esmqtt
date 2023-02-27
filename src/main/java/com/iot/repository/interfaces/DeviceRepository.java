package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceInfo;

import java.time.Instant;
import java.util.List;

public interface DeviceRepository {

    int createDevice(DeviceInfo info);

    DeviceInfo readDevice(String id);

    List<DeviceInfo> readAllDevices(int limit, int offset);

    int updateDevice(String id, DeviceInfo info);

    int deleteDevice(String id);

    int saveHeartbeat(String id, Instant ts, long hbTimeout);
}
