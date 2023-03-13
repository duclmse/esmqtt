package com.iot.repository.impl;

import com.iot.model.request.*;
import com.iot.repository.interfaces.FirmwareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FirmwareRepositoryImpl implements FirmwareRepository {

    private final JdbcTemplate jdbc;

    @Override
    public List<FirmwareInfo> getAllFirmwares(int limit, int offset) {
        var sql = "SELECT * FROM firmware_info LIMIT ? OFFSET ?";
        return jdbc.query(sql, (rs, i) -> map(rs), limit, offset);
    }

    @Override
    public FirmwareInfo getFirmware(FirmwareIndex index) {
        var sql = "SELECT * FROM firmware_info WHERE firmware_version = ? AND hardware_version = ?";
        return jdbc.queryForObject(sql, (rs, i) -> map(rs), index.firmwareVersion(), index.hardwareVersion());
    }

    @Override
    public Integer store(FirmwareInfo firmware) {
        try {
            log.info("storing firmware");
            var sql = "INSERT INTO firmware_info (`firmware_version`, `hardware_version`, `url`, `location`, `hash`, "
                + "`update_ts`) VALUES (?, ?, ?, ?, ?, NOW());";
            return jdbc.update(sql, firmware.firmwareVersion(), firmware.hardwareVersion(), firmware.url(),
                firmware.location(), firmware.hash());
        } catch (Exception e) {
            log.error("error storing firmware info", e);
            return 0;
        }
    }

    @Override
    public Integer update(FirmwareIndex index, FirmwareInfo firmware) {
        var sql = "UPDATE firmware_info SET firmware_version = ?, hardware_version = ?, url = ?, location = ?, hash = ?, update_ts = NOW() WHERE firmware_version = ? AND hardware_version = ?;";
        return jdbc.update(sql, firmware.firmwareVersion(), firmware.hardwareVersion(), firmware.url(),
            firmware.location(), firmware.hash(), index.firmwareVersion(), index.hardwareVersion());
    }

    @Override
    public Integer delete(FirmwareIndex index) {
        var sql = "DELETE FROM firmware_info WHERE firmware_version = ? AND hardware_version = ?;";
        return jdbc.update(sql, index.firmwareVersion(), index.hardwareVersion());
    }

    private FirmwareInfo map(ResultSet rs) throws SQLException {
        return (FirmwareInfo) new FirmwareInfo()
            .url(rs.getString("url"))
            .location(rs.getString("location"))
            .hash(rs.getString("hash"))
            .updateTs(rs.getTimestamp("update_ts").toInstant())
            .firmwareVersion(rs.getString("firmware_version"))
            .hardwareVersion(rs.getString("hardware_version"));
    }
}
