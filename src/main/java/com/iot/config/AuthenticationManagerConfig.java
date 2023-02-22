package com.iot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationManagerConfig implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("AuthenticationManagerConfig authenticate {}", authentication);
        if (!authentication.isAuthenticated()) return Mono.empty();

        return Mono.justOrEmpty(authentication);
    }
}