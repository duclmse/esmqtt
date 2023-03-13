package com.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.event.ApiCallEvent;
import com.iot.model.msg.DeviceInfo;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

import static com.iot.model.response.ObjectResponse.error;
import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1.0/device")
public class DeviceApi {

    private final ObjectMapper mapper = new ObjectMapper();

    private final DeviceService service;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/all")
    public Mono<ResponseEntity<ObjectResponse>> getAllDevices(
        @RequestParam(name = "limit", required = false) Integer limit,
        @RequestParam(name = "offset", required = false) Integer offset
    ) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/device/all", null));

        return service.getAllDevices(limit, offset)
            .map(device -> ok(of(0, "Get device successfully!", device)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device")))
            .doOnError(throwable -> log.error("Couldn't get device", throwable))
            .onErrorResume(e -> Mono.just(internalServerError().body(of(1, "Couldn't get device: " + e.getMessage()))));
    }

    @GetMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> getDevice(@PathVariable("device_id") String id) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/device/" + id, null));

        return service.getDevice(id)
            .map(device -> ok(of(0, "Get device successfully!", device)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device")))
            .doOnError(throwable -> log.error("Couldn't get device", throwable))
            .onErrorResume(e -> Mono.just(internalServerError().body(of(1, "Couldn't get device: " + e.getMessage()))));
    }

    @PostMapping
    public Mono<ResponseEntity<ObjectResponse>> createDevice(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, POST, "/v1.0/device", body));

        return Mono.fromCallable(() -> mapper.readValue(body, DeviceInfo.class))
            .flatMap(service::createDevice)
            .map(created -> ok(of(0, "Create " + created + " device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't create device")))
            .doOnError(throwable -> log.error("Couldn't create device: {}", throwable.getMessage()))
            .onErrorResume(throwable -> {
                if (throwable instanceof ConstraintViolationException) {
                    var stream = ((ConstraintViolationException) throwable).getConstraintViolations()
                        .stream()
                        .map(violation -> violation.getConstraintDescriptor().getMessageTemplate())
                        .toArray();
                    return Mono.just(status(BAD_REQUEST).body(error(1, "Invalid input", stream)));
                }
                return Mono.just(
                    status(INTERNAL_SERVER_ERROR).body(error(1, "Couldn't create device", throwable.getMessage())));
            });
    }

    @PutMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> updateDevice(
        @PathVariable("device_id") String id, @RequestBody String body
    ) {
        publisher.publishEvent(new ApiCallEvent(this, PUT, "/v1.0/device/" + id, body));
        return Mono.fromCallable(() -> mapper.readValue(body, DeviceInfo.class))
            .flatMap(info -> service.updateDevice(id, info))
            .map(updated -> ok(of(0, "Update " + updated + " device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't update device")))
            .doOnError(throwable -> log.error("Couldn't update device", throwable))
            .onErrorResume(
                e -> Mono.just(status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't update device: " + e.getMessage()))));
    }

    @DeleteMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> deleteDevice(@PathVariable("device_id") String id) {
        publisher.publishEvent(new ApiCallEvent(this, DELETE, "/v1.0/device/" + id, null));
        return service.deleteDevice(id)
            .map(device -> ok(of(0, "Delete " + device + " device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't delete device")))
            .doOnError(throwable -> log.error("Couldn't delete device", throwable))
            .onErrorResume(
                e -> Mono.just(status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't delete device: " + e.getMessage()))));
    }
}
