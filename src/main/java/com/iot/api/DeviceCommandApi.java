package com.iot.api;

import com.iot.model.msg.DeviceCommandHistory;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceCommandService;
import com.iot.service.interfaces.DeviceHandlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/command")
public class DeviceCommandApi {

    private final DeviceCommandService service;

    @GetMapping()
    public Mono<ResponseEntity<ObjectResponse>> getCommandHistory(
        @RequestBody CommandHistoryRequest req
    ) {
        return service.getCommandHistory(req)
            .map(history -> ok().body(of(0,"Get command history successfully!", history)));
    }

    @PostMapping
    public Mono<ResponseEntity<ObjectResponse>> sendCommand(@RequestBody String body) {
//        service.sendControlMsg("", body);
        return Mono.just(ok().body(of(0, "okie dokie!")));
    }
}
