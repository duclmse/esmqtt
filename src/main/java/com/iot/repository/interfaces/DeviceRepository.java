package com.iot.repository.interfaces;

import com.iot.model.msg.DeviceInfo;

import java.util.List;

public interface DeviceRepository {

    int createDevice(DeviceInfo info);

    DeviceInfo readDevice(String id);

    List<DeviceInfo> readDevices(String id);

    int updateDevice(String id, DeviceInfo info);

    int deleteDevice(String id);
}
