package com.example.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AuthFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private final WebClient.Builder webClientBuilder;
    private final List<String> openPaths = List.of(
            "/identity/token", "/identity/register", "/identity/forgot-password", "/identity/reset", "/users/register"
    );

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (openPaths.contains(exchange.getRequest().getURI().getPath())) {
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorization.substring(7);

        return webClientBuilder
                .build()
                .post()
                .uri("http://ms-ss-jwt/jwt/introspect")
                .bodyValue(Map.of("token", token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, error -> Mono.error(new RuntimeException("Invalid token")))
                .toBodilessEntity()
                .then(chain.filter(exchange));
    }
}
