package com.iot.repository.impl;

import com.iot.model.request.ApiHistoryRequest;
import com.iot.model.response.ApiHistory;
import com.iot.repository.interfaces.ApiHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ApiHistoryRepositoryImpl implements ApiHistoryRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int save(long ts, HttpMethod method, String endpoint, String body) {
        var sql = "INSERT INTO api_history (`ts`, `endpoint`, `method`, `body`) VALUES (?, ?, ?, ?);";
        return jdbc.update(sql, new Timestamp(ts), endpoint, method.name(), body);
    }

    @Override
    public List<ApiHistory> getHistory(ApiHistoryRequest request) {
        var sql = new StringBuilder("SELECT `ts`, `endpoint`, `method`, `body` FROM api_history ");
        var params = new ArrayList<>();
        var previous = false;
        if (request.endpoint() != null) {
            sql.append("WHERE endpoint = ? ");
            params.add(request.endpoint());
            previous = true;
        }
        if (request.method() != null) {
            sql.append(previous ? "AND " : "WHERE ").append("ts >= ? ");
            params.add(request.method());
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

    private ApiHistory map(ResultSet rs, int i) throws SQLException {
        return new ApiHistory().ts(rs.getTimestamp("ts").toInstant())
            .endpoint(rs.getString("endpoint"))
            .method(HttpMethod.valueOf(rs.getString("method")))
            .body(rs.getString("body"));
    }
}
