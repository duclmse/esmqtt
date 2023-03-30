package com.iot.service.interfaces;

import com.iot.model.msg.DeviceFullInfo;
import com.iot.model.msg.DeviceInfo;
import com.iot.model.msg.DeviceStatusHistory;
import com.iot.model.request.StatusHistoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceService {

    Mono<DeviceInfo> getDevice(String id);

    Mono<List<DeviceInfo>> getAllDevices(Integer limit, Integer offset);

    Mono<Integer> createDevice(DeviceInfo info);

    Mono<Integer> updateDevice(String id, DeviceInfo info);

    Mono<Integer> deleteDevice(String id);

    Mono<DeviceFullInfo> getLatestStatus(String id);

    Mono<DeviceStatusHistory> getStatusHistory(StatusHistoryRequest request);
}
