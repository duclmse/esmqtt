package com.iot.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1.0")
@RequiredArgsConstructor
public class GeneralApi {

    @GetMapping("/test")
    public Mono<String> get() {
        return Mono.just("okie");
    }

    @GetMapping("/command")
    public Mono<String> getCommand() {
        return Mono.just("okie");
    }
}
