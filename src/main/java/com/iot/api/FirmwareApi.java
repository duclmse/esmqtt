package com.iot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.model.event.ApiCallEvent;
import com.iot.model.request.FirmwareFile;
import com.iot.model.request.FirmwareIndex;
import com.iot.model.request.FirmwareLink;
import com.iot.model.response.ObjectResponse;
import com.iot.service.interfaces.FirmwareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.iot.model.response.ObjectResponse.of;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1.0/firmware")
public class FirmwareApi {

    private final FirmwareService service;
    private final ApplicationEventPublisher publisher;

    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public Mono<ResponseEntity<ObjectResponse>> getFirmwareInfo(
        @RequestParam(name = "limit", required = false) Integer limit,
        @RequestParam(name = "offset", required = false) Integer offset
    ) {
        var query = "limit=" + limit + "; offset=" + offset;
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/firmware", query));

        return service.getAllFirmwareInfo(limit, offset)
            .map(device -> ok(of(0, "Get firmware successfully!", device)))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't get device")))
            .doOnError(throwable -> log.error("Couldn't get device", throwable))
            .onErrorResume(e -> Mono.just(internalServerError().body(of(1, "Couldn't get device: " + e.getMessage()))));
    }

    @GetMapping("/download")
    public Mono<Void> downloadFirmware(
        @RequestParam(name = "firmware-version", required = false) String firmwareVersion,
        @RequestParam(name = "hardware-version", required = false) String hardwareVersion, ServerHttpResponse response
    ) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/firmware/download", null));

        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        return service.getFirmwareFile(new FirmwareIndex(firmwareVersion, hardwareVersion)).map(info -> {
            response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + info.name() + "");
            response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return Paths.get(info.location());
        }).flatMap(file -> {
            try {
                return zeroCopyResponse.writeWith(file, 0, Files.size(file));
            } catch (IOException e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }

    @PostMapping("/link")
    public Mono<ResponseEntity<ObjectResponse>> uploadFirmwareLink(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, GET, "/v1.0/firmware/download", body));

        return Mono.fromCallable(() -> mapper.readValue(body, FirmwareLink.class))
            .flatMap(service::storeLink)
            .map(stored -> ok(of(0, "Store " + stored + " firmware link successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't upload firmware")))
            .doOnError(throwable -> log.error("Couldn't upload firmware", throwable))
            .onErrorResume(
                e -> Mono.just(internalServerError().body(of(1, "Couldn't upload firmware: " + e.getMessage()))));
    }

    @PostMapping
    public Mono<ResponseEntity<ObjectResponse>> uploadFirmwareFile(
        @RequestPart("firmware-version") String firmwareVersion,
        @RequestPart("hardware-version") String hardwareVersion,
        @RequestPart("firmware") Mono<FilePart> firmwareFilePart
    ) {
        var query = "firmware-version: " + firmwareVersion + ", hardware-version: " + hardwareVersion;
        log.info("firmware-version: {}, hardware-version: {}", firmwareVersion, hardwareVersion);
        publisher.publishEvent(new ApiCallEvent(this, POST, "/v1.0/firmware", query));

        return firmwareFilePart.doOnNext(fp -> log.info("Received file {} : {}", fp.name(), fp.filename()))
            .flatMap(fp -> service.storeFile(
                (FirmwareFile) new FirmwareFile(fp).firmwareVersion(firmwareVersion).hardwareVersion(hardwareVersion)))
            .map(updated -> ok(of(0, "Upload firmware version '" + firmwareVersion + "' successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't upload firmware!")))
            .doOnError(throwable -> log.error("Couldn't upload firmware", throwable))
            .onErrorResume(e -> Mono.just(
                status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't upload firmware:"))));
    }

    @PutMapping("/link")
    public Mono<ResponseEntity<ObjectResponse>> updateFirmwareLink(
        @RequestParam(name="firmware-version") String firmwareVersion,
        @RequestParam(name="hardware-version") String hardwareVersion, @RequestBody String body
    ) {
        var query = "firmware-version: " + firmwareVersion + ", hardware-version: " + hardwareVersion;
        publisher.publishEvent(new ApiCallEvent(this, PUT, "/v1.0/firmware/link", query));

        var index = new FirmwareIndex(firmwareVersion, hardwareVersion);
        return Mono.fromCallable(() -> mapper.readValue(body, FirmwareLink.class))
            .flatMap(info -> service.updateLink(index, info))
            .map(updated -> ok(of(0, "Update firmware version '" + firmwareVersion + "' successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't update firmware")))
            .doOnError(throwable -> log.error("Couldn't update firmware", throwable))
            .onErrorResume(e -> Mono.just(
                status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't update firmware: " + e.getMessage()))));
    }

    @PutMapping
    public Mono<ResponseEntity<ObjectResponse>> updateFirmwareFile(
        @RequestParam("firmware-version") String firmwareVersion,
        @RequestParam("hardware-version") String hardwareVersion,
        @RequestPart("firmware-version") String newFirmwareVersion,
        @RequestPart("hardware-version") String newHardwareVersion,
        @RequestPart("firmware") Mono<FilePart> firmwareFilePart
    ) {
        var query = "firmware-version: " + firmwareVersion + ", hardware-version: " + hardwareVersion;
        publisher.publishEvent(new ApiCallEvent(this, PUT, "/v1.0/firmware", query));

        var index = new FirmwareIndex(firmwareVersion, hardwareVersion);
        return firmwareFilePart.doOnNext(fp -> log.info("Received file {} : {}", fp.name(), fp.filename()))
            .flatMap(fp -> service.updateFile(index,
                (FirmwareFile) new FirmwareFile(fp).firmwareVersion(newFirmwareVersion)
                    .hardwareVersion(newHardwareVersion)))
            .map(updated -> ok(of(0, "Update firmware version '" + firmwareVersion + "' successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't update firmware")))
            .doOnError(throwable -> log.error("Couldn't update firmware", throwable))
            .onErrorResume(e -> Mono.just(
                status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't update firmware: " + e.getMessage()))));
    }

    @DeleteMapping
    public Mono<ResponseEntity<ObjectResponse>> deleteFirmware(@RequestBody String body) {
        publisher.publishEvent(new ApiCallEvent(this, DELETE, "/v1.0/firmware", body));

        return Mono.fromCallable(() -> mapper.readValue(body, FirmwareIndex.class))
            .map(service::delete)
            .map(deleted -> ok(of(0, "Delete " + deleted + " firmware successfully!")))
            .defaultIfEmpty(badRequest().body(of(1, "Couldn't delete firmware")))
            .doOnError(throwable -> log.error("Couldn't delete firmware", throwable))
            .onErrorResume(e -> Mono.just(
                status(INTERNAL_SERVER_ERROR).body(of(1, "Couldn't delete firmware: " + e.getMessage()))));
    }
}
