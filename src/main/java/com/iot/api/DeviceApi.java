package com.iot.api;

import com.iot.model.msg.DeviceInfo;
import com.iot.model.request.ControlMessage;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@ControllerAdvice
@RequestMapping("/v1.0/device")
public class DeviceApi {

    private final DeviceService service;

    @GetMapping("/all")
    public Mono<ResponseEntity<ObjectResponse>> getAllDevices(
        @RequestParam(name = "limit", required = false) Integer limit,
        @RequestParam(name = "offset", required = false) Integer offset
    ) {
        log.info("get all devices");
        return service.getAllDevices(limit, offset)
            .map(device -> ok(of(0, "Get device successfully!", device)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device")))
            .doOnError(throwable -> log.error("Couldn't get device", throwable))
            .onErrorReturn(status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't get device")));
    }

    @GetMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> getDevice(@PathVariable("device_id") String id) {
        log.info("get device");
        return service.getDevice(id)
            .map(device -> ok(of(0, "Get device successfully!", device)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device")))
            .doOnError(throwable -> log.error("Couldn't get device", throwable))
            .onErrorReturn(status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't get device")));
    }

    @PostMapping
    public Mono<ResponseEntity<ObjectResponse>> createDevice(@RequestBody @Valid Mono<DeviceInfo> info) {
        return info.map(service::createDevice)
            .map(device -> ok(of(0, "Create device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't create device")))
            .doOnError(throwable -> {
                if (throwable instanceof WebExchangeBindException e) {
                    log.error("Invalid input: {}", e.getDetailMessageArguments());
                } else {
                    log.error("Couldn't create device:", throwable);
                }
            });
    }

    @PutMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> updateDevice(
        @PathVariable("device_id") String id, @RequestBody DeviceInfo info
    ) {
        return service.updateDevice(id, info)
            .map(device -> ok(of(0, "Update device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't update device")))
            .doOnError(throwable -> log.error("Couldn't update device", throwable))
            .onErrorReturn(Exception.class, status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't update device")));
    }

    @DeleteMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> deleteDevice(@PathVariable("device_id") String id) {
        return service.deleteDevice(id)
            .map(device -> ok(of(0, "Delete device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't delete device")))
            .doOnError(throwable -> log.error("Couldn't delete device", throwable))
            .onErrorReturn(Exception.class, status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't delete device")));
    }

    @PostMapping("/{device_id}")
    public Mono<ResponseEntity<ObjectResponse>> controlDevice(
        @PathVariable("device_id") String id, @RequestBody ControlMessage msg
    ) {
        return service.controlDevice(id, msg)
            .map(device -> ok(of(0, "Create device successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't create device")))
            .doOnError(throwable -> log.error("Couldn't create device", throwable))
            .onErrorReturn(Exception.class, status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't get device")));
    }

}
