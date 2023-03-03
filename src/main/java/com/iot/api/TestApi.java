package com.iot.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestApi {

    private final Path basePath = Paths.get("./files");

    @GetMapping()
    public Mono<String> test() {
        return Mono.just("ok");
    }

    @PostMapping("/file/single")
    public Mono<Void> upload(
        @RequestPart("user-name") String name, @RequestPart("fileToUpload") Mono<FilePart> filePartMono
    ) {
        log.info("user : {}", name);
        return filePartMono.doOnNext(fp -> log.info("Received File : {} : {}", fp.filename(), fp.name()))
            .flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename())))
            .then();
    }

    @PostMapping("/file/multi")
    public Mono<Void> upload(@RequestPart("files") Flux<FilePart> partFlux) {
        return partFlux.doOnNext(fp -> log.info("{}: {}", fp.filename(), fp.name()))
            .flatMap(fp -> fp.transferTo(basePath.resolve(fp.filename())))
            .then();
    }
}
