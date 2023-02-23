package com.iot.config;

import com.iot.model.response.ObjectResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.iot.model.response.ObjectResponse.error;

@RestControllerAdvice
public class InvalidRequestConfig {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ObjectResponse>> handleValidationExceptions(ConstraintViolationException ex) {
        return Mono.just(ResponseEntity.of(Optional.of(error(1, "Invalid input", ex.getConstraintViolations()))));
    }
}
