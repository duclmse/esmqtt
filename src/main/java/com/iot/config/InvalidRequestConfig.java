package com.iot.config;

import com.iot.model.response.ObjectResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static com.iot.model.response.ObjectResponse.error;
import static org.springframework.http.ResponseEntity.of;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class InvalidRequestConfig {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ObjectResponse>> handleValidationExceptions(ConstraintViolationException ex) {
        return Mono.just(of(Optional.of(error(1, "Invalid input", ex.getConstraintViolations()))));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Mono<ResponseEntity<ObjectResponse>> handleNotFound(EmptyResultDataAccessException ex) {
        return Mono.just(status(HttpStatus.NOT_FOUND).body(error(1, "Not found", ex.getMessage())));
    }
}
