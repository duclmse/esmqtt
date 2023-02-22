package com.iot.repository.iml;

import com.iot.model.CommandInfo;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.repository.interfaces.DeviceControlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceControlRepositoryImpl implements DeviceControlRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int saveCommand(String id, Instant ts, String command) {
        var sql = "INSERT INTO device_command_history(device_id, ts, message) VALUES (?, ?, ?);";
        return jdbc.update(sql, id, ts.toEpochMilli(), command);
    }

    @Override
    public List<CommandInfo> getCommandHistory(CommandHistoryRequest request) {
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
            params.add(request.from());
            previous = true;
        }
        if (request.to() != null) {
            sql.append(previous ? "AND " : "WHERE ").append("ts <= ? ");
            params.add(request.to());
        }
        sql.append("LIMIT ? OFFSET ?");
        params.add(request.limit() != null ? request.limit() : 100);
        params.add(request.offset() != null ? request.offset() : 0);

        return jdbc.query(sql.toString(), this::map, params.toArray());
    }

    private CommandInfo map(ResultSet rs, int i) {
        return null;
    }

}
