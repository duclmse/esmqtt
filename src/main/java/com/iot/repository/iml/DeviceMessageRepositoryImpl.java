package com.iot.repository.iml;

import com.iot.model.msg.DeviceMessage;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.repository.interfaces.DeviceMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.Instant;
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
        return jdbc.update(sql, id, ts.toEpochMilli(), msg);
    }

    @Override
    public List<DeviceMessage> getMessages(MessageHistoryRequest request) {
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

    private DeviceMessage map(ResultSet rs, int i) {
        log.info("implement me ");
        return new DeviceMessage();
    }
}
