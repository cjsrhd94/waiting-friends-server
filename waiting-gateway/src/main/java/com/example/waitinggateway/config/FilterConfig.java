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

    private final AuthorizationHeaderFilter authorizationHeaderFilter;
    private final LoggingFilter loggingFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // user-service
                .route("user-service",
                        r -> r.path(
                                        "/api/v1/users/health",
                                        "/api/v1/users/login",
                                        "/api/v1/users/sign-up",
                                        "/api/v1/users/reissue",
                                        "/api/v1/users/logout"
                                )
                                .filters(f -> f
                                        .removeRequestHeader("Cookie")
                                        .rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                                        .filter(loggingFilter.apply(new LoggingFilter.Config("Spring Cloud Gateway Logging Filter", true, true)))
                                )
                                .uri("lb://user-service")
                )

                .route("user-service",
                        r -> r.path(
                                        "/api/v1/users/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader("Cookie")
                                        .rewritePath("/user-service/(?<segment>.*)", "/${segment}")
                                        .filter(loggingFilter.apply(new LoggingFilter.Config("Spring Cloud Gateway Logging Filter", true, true)))
                                        .filter(authorizationHeaderFilter.apply(new AuthorizationHeaderFilter.Config()))
                                )
                                .uri("lb://user-service")
                )

                // reservation-service
                .route("reservation-service",
                        r -> r.path(
                                        "/api/v1/reservations/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader("Cookie")
                                        .rewritePath("/reservation-service/(?<segment>.*)", "/${segment}")
                                        .filter(loggingFilter.apply(new LoggingFilter.Config("Spring Cloud Gateway Logging Filter", true, true)))
                                )
                                .uri("lb://reservation-service")
                )

                // spot-service
                .route("spot-service",
                        r -> r.path(
                                        "/api/v1/spots/**"
                                )
                                .filters(f -> f
                                        .removeRequestHeader("Cookie")
                                        .rewritePath("/spot-service/(?<segment>.*)", "/${segment}")
                                        .filter(loggingFilter.apply(new LoggingFilter.Config("Spring Cloud Gateway Logging Filter", true, true)))
                                )
                                .uri("lb://spot-service")
                )
                .build();
    }
}
