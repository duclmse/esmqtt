package com.iot.service.impl;

import com.iot.model.request.*;
import com.iot.repository.interfaces.FirmwareRepository;
import com.iot.service.interfaces.FirmwareService;
import com.iot.service.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirmwareServiceImpl implements FirmwareService {

    private final StorageService storage;
    private final FirmwareRepository repository;

    @Override
    public Mono<List<FirmwareInfo>> getAllFirmwareInfo(Integer limit, Integer offset) {
        int l = limit == null ? 100 : limit;
        int o = offset == null ? 0 : offset;
        log.info("svc getAllFirmwareInfo limit {} offset {}", l, o);
        return Mono.fromCallable(() -> repository.getAllFirmwares(l, o));
    }

    @Override
    public Mono<FirmwareInfo> getFirmwareFile(FirmwareIndex index) {
        return Mono.fromCallable(() -> repository.getFirmware(index));
    }

    @Override
    public Mono<Integer> storeFile(FirmwareFile firmwareFile) {
        return storage.store(firmwareFile)
            .map(o -> repository.store(firmwareFile))
            .doOnNext(saved -> log.info("saved {} firmware info successfully!", saved));
    }

    @Override
    public Mono<Integer> storeLink(FirmwareLink firmwareLink) {
        return Mono.fromCallable(() -> repository.store(firmwareLink));
    }

    @Override
    public Mono<Integer> updateFile(FirmwareIndex index, FirmwareFile firmwareFile) {
        return storage.store(firmwareFile)
            .map(o -> repository.update(index, firmwareFile))
            .doOnNext(saved -> log.info("saved {} firmware info successfully!", saved));
    }

    @Override
    public Mono<Integer> updateLink(FirmwareIndex index, FirmwareLink firmwareLink) {
        return Mono.fromCallable(() -> repository.update(index, firmwareLink));
    }

    @Override
    public Mono<Integer> delete(FirmwareIndex index) {
        return Mono.fromCallable(() -> repository.getFirmware(index)).doOnNext(info -> {
            try {
                storage.delete(info.location());
            } catch (IOException ignored) {
            }
        }).map(info -> repository.delete(index));
    }
}
