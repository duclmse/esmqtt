package com.iot.service.interfaces;

import com.iot.model.msg.DeviceInfo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceService {

    Mono<DeviceInfo> getDevice(String id);

    Mono<List<DeviceInfo>> getAllDevices(Integer limit, Integer offset);

    Mono<Integer> createDevice(DeviceInfo info);

    Mono<Integer> updateDevice(String id, DeviceInfo info);

    Mono<Integer> deleteDevice(String id);

}
