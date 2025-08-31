package com.example.waitinggateway.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

@Configuration
public class TraceConfig {

    @PostConstruct
    public void init() {
        // Reactor Context에서 tracing 정보 전파 활성화
        Hooks.enableAutomaticContextPropagation();
    }
}
