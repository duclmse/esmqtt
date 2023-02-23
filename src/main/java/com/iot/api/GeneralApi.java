package com.iot.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iot.model.response.ObjectResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/v1.0")
@RequiredArgsConstructor
public class GeneralApi {

    private final JdbcTemplate jdbc;

    @GetMapping("/test")
    public Mono<String> get() {
        return Mono.just("okie");
    }

    @PostMapping("/sql/query")
    public Mono<ResponseEntity<ObjectResponse>> query(@RequestBody String sql) {
        return Mono.justOrEmpty(jdbc)
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull(j -> j.query(sql, rs -> {
                var metadata = rs.getMetaData();
                var columnCount = metadata.getColumnCount();
                var dataList = new DataList();
                for (var i = 1; i <= columnCount; i++) {
                    dataList.columns.add(metadata.getColumnName(i));
                }

                while (rs.next()) {
                    var row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    dataList.data.add(row);
                }
                return dataList;
            }))
            .doOnNext(l -> log.info("SQL\n\t{}\n  -> return {} column(s) x {} row(s)", sql.replace("\n", "\n\t"), l.columns.size(), l.data.size()))
            .map(l -> ok().body(of(0, "OK!", l)))
            .doOnError(e -> log.error("SQL\n\t{}\n  -> error {}", sql, e.getMessage()))
            .onErrorResume(e -> Mono.just(ok().body(of(1, e.getMessage()))));
    }

    @PostMapping("/sql/update")
    public Mono<ResponseEntity<ObjectResponse>> update(@RequestBody String sql) {
        return Mono.justOrEmpty(jdbc)
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull(j -> j.update(sql))
            .doOnNext(l -> log.info("SQL\n\t{}\n  -> updated {} row(s)", sql.replace("\n", "\n\t"), l))
            .map(l -> ok().body(of(0, "OK!", l)))
            .doOnError(e -> log.error("SQL\n\t{}\n  -> {}", sql, e.getMessage()))
            .onErrorResume(e -> Mono.just(ok().body(of(1, e.getMessage()))));
    }

    @Data
    @Accessors(fluent = true)
    private static class DataList {

        @JsonProperty("columns")
        private List<String> columns = new ArrayList<>();

        @JsonProperty("data")
        private List<List<Object>> data = new ArrayList<>();
    }
}
