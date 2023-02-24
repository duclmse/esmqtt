package com.iot.repository.iml;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceCommandRepository;
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
public class DeviceCommandRepositoryImpl implements DeviceCommandRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int saveCommand(String id, Instant ts, String command) {
        var sql = "INSERT INTO device_command_history(device_id, ts, message) VALUES (?, ?, ?);";
        return jdbc.update(sql, id, Timestamp.from(ts), command);
    }

    @Override
    public List<DeviceCommandHistory> getCommandHistory(CommandHistoryRequest request) {
        var sql = new StringBuilder("SELECT device_id, ts, message FROM device_command_history ");
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
        log.debug("sql {}", sql);
        return jdbc.query(sql.toString(), this::map, params.toArray());
    }

    private DeviceCommandHistory map(ResultSet rs, int i) throws SQLException {
        return new DeviceCommandHistory().id(rs.getString("device_id"))
            .ts(rs.getTimestamp("ts").toInstant())
            .msg(rs.getString("msg"));
    }
}
