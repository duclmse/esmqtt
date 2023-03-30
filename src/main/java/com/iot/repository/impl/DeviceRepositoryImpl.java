package com.iot.repository.impl;

import com.iot.model.msg.DeviceFullInfo;
import com.iot.model.msg.DeviceInfo;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.msg.DeviceStatusHistory;
import com.iot.model.request.StatusHistoryRequest;
import com.iot.repository.interfaces.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int createDevice(DeviceInfo i) {
        var sql = "INSERT INTO device(`id`, `name`, `ip`, `version`, `hub_id`, `product_id`, `device_type`, `home_id`) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        return jdbc.update(sql, i.id(), i.name(), i.ip(), i.version(), i.hubId(), i.productId(), i.deviceType(),
            i.homeId());
    }

    @Override
    public DeviceFullInfo readDevice(String id) {
        var sql = "SELECT * FROM device WHERE id = ?;";
        return jdbc.queryForObject(sql, (rs, i) -> this.map(rs), id);
    }

    @Override
    public List<DeviceInfo> readAllDevices(int limit, int offset) {
        var sql = "SELECT * FROM device LIMIT ? OFFSET ?;";
        return jdbc.query(sql, (rs, i) -> this.map(rs), limit, offset);
    }

    @Override
    public int updateDevice(String id, DeviceInfo info) {
        var sql = "UPDATE device SET `id` = ?, `name` = ?, `ip` = ?, `version` = ?, `hub_id` = ?, `product_id` = ?, "
            + "`device_type` = ?, `home_id` = ? WHERE id = ?;";
        return jdbc.update(sql, info.id(), info.name(), info.ip(), info.version(), info.hubId(), info.productId(),
            info.deviceType(), info.homeId(), id);
    }

    @Override
    public int deleteDevice(String id) {
        var sql = "DELETE FROM device WHERE id = ?;";
        return jdbc.update(sql, id);
    }

    @Override
    public int saveHeartbeat(String id, Instant ts, long hbTimeout) {
        var expectHb = ts.plus(hbTimeout, ChronoUnit.MILLIS);
        var sql = "UPDATE device SET heartbeat = ?, expect_hb = ? WHERE id = ?;";
        return jdbc.update(sql, Timestamp.from(ts), Timestamp.from(expectHb), id);
    }

    @Override
    public DeviceFullInfo getLatestStatus(String id) {
        var sql = "SELECT * FROM device_status_history WHERE device_id = ? ORDER BY ts DESC LIMIT 1;";
        var statusList = jdbc.query(sql, (rs, i) -> this.mapStatus(rs), id);
        if (statusList.isEmpty()) {
            return null;
        }
        var info = readDevice(id);
        info.status(statusList.get(0));
        return info;
    }

    @Override
    public DeviceStatusHistory getStatusHistory(StatusHistoryRequest req) {
        var info = DeviceStatusHistory.from(readDevice(req.deviceId()));

        // language=sql
        var sql = new StringBuilder("SELECT * FROM device_status_history WHERE device_id = ? ");
        var params = new ArrayList<>();
        params.add(req.deviceId());

        if (req.from() != null) {
            sql.append("AND ts >= ? ");
            params.add(Date.from(req.from()));
        }
        if (req.to() != null) {
            sql.append("AND ts <= ? ");
            params.add(Date.from(req.to()));
        }
        sql.append("LIMIT ? OFFSET ?;");
        params.add(req.limit());
        params.add(req.offset());
        var status = jdbc.query(sql.toString(), (rs, i) -> this.mapStatus(rs), params.toArray());
        return info.history(status);
    }

    private DeviceStatus mapStatus(ResultSet rs) throws SQLException {
        return new DeviceStatus().switch1(rs.getBoolean("switch_1"))
            .countdown1(rs.getInt("countdown_1"))
            .addEle(rs.getInt("add_ele"))
            .curCurrent(rs.getInt("cur_current"))
            .curPower(rs.getInt("cur_power"))
            .curVoltage(rs.getInt("cur_voltage"))
            .testBit(rs.getInt("test_bit"))
            .voltageCoe(rs.getInt("voltage_coe"))
            .electricCoe(rs.getInt("electric_coe"))
            .powerCoe(rs.getInt("power_coe"))
            .electricityCoe(rs.getInt("electricity_coe"))
            .fault(rs.getString("fault"))
            .relayStatus(rs.getString("relay_status"))
            .cycleTime(rs.getString("cycle_time"))
            .randomTime(rs.getString("random_time"));
    }

    private DeviceFullInfo map(ResultSet rs) throws SQLException {
        var info = new DeviceFullInfo();
        info.id(rs.getString("id"))
            .name(rs.getString("name"))
            .ip(rs.getString("ip"))
            .version(rs.getString("version"))
            .hubId(rs.getString("hub_id"))
            .productId(rs.getString("product_id"))
            .deviceType(rs.getString("device_type"))
            .homeId(rs.getString("home_id"));
        var heartbeat = rs.getTimestamp("heartbeat");
        if (heartbeat != null) {
            info.heartbeat(heartbeat.toInstant());
        }
        var expectHb = rs.getTimestamp("expect_hb");
        if (expectHb != null) {
            info.expectHeartbeat(expectHb.toInstant());
        }
        return info;
    }
}
