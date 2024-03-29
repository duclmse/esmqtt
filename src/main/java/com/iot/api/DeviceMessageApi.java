package com.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.event.ApiCallEvent;
import com.iot.model.request.MessageHistoryRequest;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import static com.iot.model.response.ObjectResponse.error;
import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/message")
public class DeviceMessageApi {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Validator validator;
    private final DeviceMessageService service;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/history")
    public Mono<ResponseEntity<ObjectResponse>> getMessageHistory(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/command", body));
        return Mono.fromCallable(() -> mapper.readValue(body, MessageHistoryRequest.class))
            .flatMap(req -> {
                var violations = validator.validate(req);
                return violations.isEmpty() ? Mono.just(req) : Mono.error(new ConstraintViolationException(violations));
            })
            .flatMap(service::getMessageHistory)
            .map(history -> ok(of(0, "Get message history successfully!", history)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device message history")))
            .doOnError(throwable -> log.error("Couldn't get device message history: {}", throwable.getMessage()))
            .onErrorResume(throwable -> {
                if (throwable instanceof ConstraintViolationException) {
                    var stream = ((ConstraintViolationException) throwable).getConstraintViolations()
                        .stream()
                        .map(violation -> violation.getConstraintDescriptor().getMessageTemplate())
                        .toArray();
                    return Mono.just(status(BAD_REQUEST).body(error(1, "Invalid input", stream)));
                }
                return Mono.just(
                    status(INTERNAL_SERVER_ERROR).body(error(1, "Couldn't get device message history", throwable.getMessage())));
            });
    }
}
