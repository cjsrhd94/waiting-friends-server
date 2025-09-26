package com.example.waitinggateway.config;

import com.example.waitinggateway.filter.AuthorizationHeaderFilter;
import com.example.waitinggateway.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {
    private static final String COOKIE_HEADER = "Cookie";
    private static final String REPLACE_PATH = "/${segment}";
    private static final String LOGGING_FILTER_BASE_MESSAGE = "Spring Cloud Gateway Logging Filter";

    private final AuthorizationHeaderFilter authorizationHeaderFilter;
    private final LoggingFilter loggingFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // user-service
                .route("user-service",
                        r -> r.path(
                                        "/api/users/health",
                                        "/api/users/login",
                                        "/api/users/sign-up",
                                        "/api/users/reissue",
                                        "/api/users/logout"
                                )
                                .filters(f -> f
                                        .removeRequestHeader(COOKIE_HEADER)
                                        .rewritePath("/user-service/(?<segment>.*)", REPLACE_PATH)
                                        .filter(loggingFilter.apply(new LoggingFilter.Config(LOGGING_FILTER_BASE_MESSAGE, true, true)))
                                        .circuitBreaker(config -> config
                                                .setName("userCircuitBreaker")
                                                .setFallbackUri("forward:/fallback/user-service")
                                        )
                                )
                                .uri("lb://user-service")
                )

                .route("user-service",
                        r -> r.path(
                                        "/api/users/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader(COOKIE_HEADER)
                                        .rewritePath("/user-service/(?<segment>.*)", REPLACE_PATH)
                                        .filter(loggingFilter.apply(new LoggingFilter.Config(LOGGING_FILTER_BASE_MESSAGE, true, true)))
                                        .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                        .circuitBreaker(config -> config
                                                .setName("userCircuitBreaker")
                                                .setFallbackUri("forward:/fallback/user-service")
                                        )
                                )
                                .uri("lb://user-service")
                )

                // reservation-service
                .route("reservation-service",
                        r -> r.path(
                                        "/api/reservations/**",
                                        "/api/spots/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader(COOKIE_HEADER)
                                        .rewritePath("/reservation-service/(?<segment>.*)", REPLACE_PATH)
                                        .filter(loggingFilter.apply(new LoggingFilter.Config(LOGGING_FILTER_BASE_MESSAGE, true, true)))
                                        .circuitBreaker(config -> config
                                                .setName("reservationCircuitBreaker")
                                                .setFallbackUri("forward:/fallback/reservation-service")
                                        )
                                )
                                .uri("lb://reservation-service")
                )

                .route("reservation-service",
                        r -> r.path(
                                        "/admin/reservations/**",
                                        "/admin/spots/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader(COOKIE_HEADER)
                                        .rewritePath("/reservation-service/(?<segment>.*)", REPLACE_PATH)
                                        .filter(loggingFilter.apply(new LoggingFilter.Config(LOGGING_FILTER_BASE_MESSAGE, true, true)))
                                        .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                        .circuitBreaker(config -> config
                                                .setName("reservationCircuitBreaker")
                                                .setFallbackUri("forward:/fallback/reservation-service")
                                        )
                                )
                                .uri("lb://reservation-service")
                )
                .build();
    }
}
