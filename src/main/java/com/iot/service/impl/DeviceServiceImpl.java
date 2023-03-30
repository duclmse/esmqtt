package com.iot.service.impl;

import com.iot.model.msg.DeviceFullInfo;
import com.iot.model.msg.DeviceInfo;
import com.iot.model.msg.DeviceStatusHistory;
import com.iot.model.request.StatusHistoryRequest;
import com.iot.repository.interfaces.DeviceRepository;
import com.iot.service.interfaces.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repository;
    private final Validator validator;

    @Override
    public Mono<DeviceInfo> getDevice(String id) {
        return Mono.fromCallable(() -> repository.readDevice(id));
    }

    @Override
    public Mono<List<DeviceInfo>> getAllDevices(Integer limit, Integer offset) {
        int l = limit == null ? 100 : limit;
        int o = offset == null ? 0 : offset;
        log.info("svc getAllDevices limit {} offset {}", l, o);
        return Mono.fromCallable(() -> repository.readAllDevices(l, o));
    }

    @Override
    public Mono<Integer> createDevice(DeviceInfo deviceInfo) {
        log.debug("device svc - create device");
        return Mono.just(deviceInfo)
            .flatMap(info -> {
                var violations = validator.validate(info);
                return violations.isEmpty()
                    ? Mono.just(info)
                    : Mono.error(new ConstraintViolationException(violations));
            })
            .map(repository::createDevice)
            .onErrorMap(DataIntegrityViolationException.class, e -> new Exception(e.getMessage()));
    }

    @Override
    public Mono<Integer> updateDevice(String id, DeviceInfo info) {
        return Mono.fromCallable(() -> repository.updateDevice(id, info));
    }

    @Override
    public Mono<Integer> deleteDevice(String id) {
        return Mono.fromCallable(() -> repository.deleteDevice(id));
    }

    @Override
    public Mono<DeviceFullInfo> getLatestStatus(String id) {
        return Mono.fromCallable(() -> repository.getLatestStatus(id));
    }

    @Override
    public Mono<DeviceStatusHistory> getStatusHistory(StatusHistoryRequest request) {
        return Mono.fromCallable(() -> repository.getStatusHistory(request));
    }
}
