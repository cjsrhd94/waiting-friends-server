package com.example.waitinggateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private static final String SECRET = "a6894769aa24941d4ae869fc69d30ddd60cdbbce7b85d7cd1f2660e406b4ea037e443ac47d058a2a00364831a13bbafde895d6a42e0648f1b352c226197b077d";
    private static final String BEARER_TYPE = "Bearer ";

    public static class Config {
        // 필요한 설정을 추가할 수 있습니다.
    }

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "로그인이 필요한 서비스입니다.");
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace(BEARER_TYPE, "");

            if (!validateToken(jwt)) {
                return onError(exchange, "JWT 토큰이 유효하지 않습니다.");
            }

            String email = parsePayload(jwt)
                    .get("email", String.class);
            String role = parsePayload(jwt)
                    .get("role", String.class);

            ServerHttpRequest jwtInfo = request.mutate()
                    .header("email", email)
                    .header("role", role)
                    .build();

            return chain.filter(exchange.mutate().request(jwtInfo).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        log.error(err);
        return response.setComplete();
    }

    private boolean validateToken(String token) {
        boolean value = true;

        try {
            parsePayload(token);
        } catch (Exception e) {
            value = false;
        }

        return value;
    }

    public String getEmail(String token) {
        return parsePayload(token)
                .get("email", String.class);
    }

    public String getRole(String token) {
        return parsePayload(token)
                .get("role", String.class);
    }

    private Claims parsePayload(String token) {
        return Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKeySpec generateSecretKey() {
        return new SecretKeySpec(
                SECRET.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }
}
