package com.iot.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.event.ApiCallEvent;
import com.iot.model.msg.DeviceStatus;
import com.iot.model.request.CommandHistoryRequest;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.iot.model.response.ObjectResponse.error;
import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/command")
public class DeviceCommandApi {

    private final ObjectMapper mapper = new ObjectMapper();

    private final DeviceCommandService service;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/history")
    public Mono<ResponseEntity<ObjectResponse>> getCommandHistory(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, POST, "/v1.0/command/history", body));

        return Mono.fromCallable(() -> mapper.readValue(body, CommandHistoryRequest.class))
            .flatMap(service::getCommandHistory)
            .map(history -> ok(of(0, "Get command history successfully!", history)))
            .onErrorResume(JsonProcessingException.class,
                throwable -> Mono.just(badRequest().body(of(1, "Invalid input"))))
            .onErrorResume(Exception.class,
                throwable -> Mono.just(internalServerError().body(error(1, "error", throwable))));
    }

    @PostMapping("/{device-id}")
    public Mono<ResponseEntity<ObjectResponse>> sendCommand(
        @PathVariable("device-id") String deviceId, @RequestBody String body
    ) {
        log.info("control device {}, msg {}", deviceId, body);
        publisher.publishEvent(new ApiCallEvent(this, POST, "/v1.0/command/" + deviceId, body));

        return Mono.fromCallable(() -> mapper.readValue(body, DeviceStatus.class))
            .flatMap(msg -> service.sendControlMsg(deviceId, msg))
            .map(done -> ok(of(0, "Published command")))
            .defaultIfEmpty(ok(of(0, "published command")))
            .onErrorResume(JsonProcessingException.class,
                throwable -> Mono.just(badRequest().body(of(1, "Invalid input"))))
            .onErrorResume(Exception.class,
                throwable -> Mono.just(internalServerError().body(error(1, "error", throwable))));
    }
}
