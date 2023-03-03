package com.iot.service.iml;

import com.iot.model.msg.DeviceInfo;
import com.iot.repository.interfaces.DeviceRepository;
import com.iot.service.interfaces.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repository;

    @Override
    public Mono<DeviceInfo> getDevice(String id) {
        return Mono.justOrEmpty(repository.readDevice(id));
    }

    @Override
    public Mono<List<DeviceInfo>> getAllDevices(Integer limit, Integer offset) {
        int l = limit == null ? 100 : limit;
        int o = offset == null ? 0 : offset;
        log.info("svc getAllDevices limit {} offset {}", l, o);
        return Mono.justOrEmpty(repository.readAllDevices(l, o));
    }

    @Override
    public Mono<Integer> createDevice(DeviceInfo info) {
        log.debug("device svc - create device");
        return Mono.fromCallable(() -> repository.createDevice(info))
            .onErrorMap(DataIntegrityViolationException.class, e -> new Exception(e.getMessage()));
    }

    @Override
    public Mono<Integer> updateDevice(String id, DeviceInfo info) {
        return Mono.justOrEmpty(repository.updateDevice(id, info));
    }

    @Override
    public Mono<Integer> deleteDevice(String id) {
        return Mono.justOrEmpty(repository.deleteDevice(id));
    }
}
