package com.iot.service.interfaces;

import com.iot.model.request.FirmwareFile;
import com.iot.model.request.FirmwareIndex;
import com.iot.model.request.FirmwareLink;
import com.iot.model.request.FirmwareInfo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FirmwareService {

    Mono<List<FirmwareInfo>> getAllFirmwareInfo(Integer limit, Integer offset);

    Mono<FirmwareInfo> getFirmwareFile(FirmwareIndex index);

    Mono<Integer> storeFile(FirmwareFile firmwareFile);

    Mono<Integer> storeLink(FirmwareLink firmwareLink);

    Mono<Integer> updateFile(FirmwareIndex index, FirmwareFile firmwareFile);

    Mono<Integer> updateLink(FirmwareIndex index, FirmwareLink firmwareFile);

    Mono<Integer> delete(FirmwareIndex index);
}
