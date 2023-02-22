package com.iot.api;

import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceHandlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/message")
public class DeviceMessageApi {

    private final DeviceHandlingService service;

    @GetMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> getMessageHistory(
        @PathVariable("device_id") String id
    ) {

        return Mono.just(ok().body(of(0, "okie dokie!")));
    }
}
