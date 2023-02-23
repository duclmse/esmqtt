package com.iot.api;

import com.iot.model.event.ApiCallEvent;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/message")
public class DeviceMessageApi {

    private final DeviceMessageService service;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/history")
    public Mono<ResponseEntity<ObjectResponse>> getMessageHistory(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/command", body));
        return Mono.just(ok().body(of(0, "okie dokie!")));
    }
}
