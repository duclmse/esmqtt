package com.iot.repository.impl;

import com.iot.model.msg.DeviceMessageHistory;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceMessageRepositoryImpl implements DeviceMessageRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int saveMessage(String id, Instant ts, String msg) {
        var sql = "INSERT INTO device_message_history(device_id, ts, message) VALUES (?, ?, ?);";
        return jdbc.update(sql, id, Timestamp.from(ts), msg);
    }

    @Override
    public int saveStatus(String id, Instant ts, DeviceStatus msg) {
        var sql = "INSERT INTO device_status_history(`device_id`, `ts`, `switch_1`, `countdown_1`, `add_ele`, "
            + "`cur_current`, `cur_power`, `cur_voltage`, `test_bit`, `voltage_coe`, `electric_coe`, `power_coe`, "
            + "`electricity_coe`, `fault`, `relay_status`, `cycle_time`, `random_time`) VALUES (?, ?, ?, ?, ?, ?, ?, "
            + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        return jdbc.update(sql, id, Timestamp.from(ts), msg.switch1(), msg.countdown1(), msg.addEle(), msg.curCurrent(),
            msg.curPower(), msg.curVoltage(), msg.testBit(), msg.voltageCoe(), msg.electricCoe(), msg.powerCoe(),
            msg.electricCoe(), msg.fault(), msg.relayStatus(), msg.cycleTime(), msg.randomTime());
    }

    @Override
    public List<DeviceMessageHistory> getMessages(MessageHistoryRequest request) {
        var sql = new StringBuilder("SELECT device_id, ts, message FROM device_message_history ");
        var params = new ArrayList<>();
        var previous = false;
        if (request.deviceId() != null) {
            sql.append("WHERE device_id = ? ");
            params.add(request.deviceId());
            previous = true;
        }
        if (request.from() != null) {
            sql.append(previous ? "AND " : "WHERE ").append("ts >= ? ");
            params.add(Timestamp.from(request.from().toInstant(ZoneOffset.UTC)));
            previous = true;
        }
        if (request.to() != null) {
            sql.append(previous ? "AND " : "WHERE ").append("ts <= ? ");
            params.add(Timestamp.from(request.to().toInstant(ZoneOffset.UTC)));
        }
        sql.append("LIMIT ? OFFSET ?");
        params.add(request.limit() != null ? request.limit() : 100);
        params.add(request.offset() != null ? request.offset() : 0);

        return jdbc.query(sql.toString(), this::map, params.toArray());
    }

    private DeviceMessageHistory map(ResultSet rs, int i) throws SQLException {
        return new DeviceMessageHistory().id(rs.getString("device_id"))
            .ts(rs.getTimestamp("ts").toInstant())
            .msg(rs.getString("message"));
    }
}
