package com.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.request.ApiHistoryRequest;
import com.iot.model.response.ObjectResponse;
import com.iot.repository.interfaces.ApiHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1.0/api")
public class HistoryApi {

    private final ApiHistoryRepository apiHistoryRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/history")
    public Mono<ResponseEntity<ObjectResponse>> apiHistory(@RequestBody String body) {
        return Mono.fromCallable(() -> mapper.readValue(body, ApiHistoryRequest.class))
            .map(apiHistoryRepository::getHistory)
            .map(history -> ok(of(0, "okie", history)));
    }
}
