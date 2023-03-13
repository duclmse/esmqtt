package com.iot.repository.interfaces;

import com.iot.model.request.FirmwareIndex;
import com.iot.model.request.FirmwareInfo;

import java.util.List;

public interface FirmwareRepository {

    List<FirmwareInfo> getAllFirmwares(int limit, int offset);

    FirmwareInfo getFirmware(FirmwareIndex index);

    Integer store(FirmwareInfo firmware);

    Integer update(FirmwareIndex index, FirmwareInfo firmware);

    Integer delete(FirmwareIndex index);
}
