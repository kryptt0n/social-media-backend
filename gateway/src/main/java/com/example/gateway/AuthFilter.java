package com.example.gateway;

import com.example.gateway.dto.UserMeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
            "/identity/token", "/identity/register", "/identity/forgot-password", "/identity/reset", "/users/register",
            "/oauth/authorize", "/oauth/callback", "/oauth/username"
    );

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (openPaths.stream().anyMatch(path -> exchange.getRequest().getURI().getPath().contains(path))) {
            return chain.filter(exchange);
        }

        HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("token");


        if (tokenCookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = tokenCookie.getValue();
        log.warn("Token auth: {}", token);

        var webClient = webClientBuilder.build();

        Mono<Void> validateTokenMono = webClient
                .post()
                .uri("http://ms-ss-jwt/jwt/introspect")
                .bodyValue(Map.of("token", token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        resp -> Mono.error(new RuntimeException("Invalid token")))
                .toBodilessEntity()
                .then();

        Mono<UserMeDTO> fetchMeMono = webClient
                .get()
                .uri("http://ms-os-identity/identity/me")
                .cookie("token", token)
                .retrieve()
                .bodyToMono(UserMeDTO.class);

        return validateTokenMono
                .then(fetchMeMono)
                .flatMap(meUser -> {

                    log.warn("Fetched meUser: {}", meUser);

                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", String.valueOf(meUser.userId()))
                            .header("X-Username", meUser.username())
                            .build();

                    log.warn("Gateway will forward headers: X-User-Id={}, X-Username={}",
                            mutatedRequest.getHeaders().getFirst("X-User-Id"),
                            mutatedRequest.getHeaders().getFirst("X-Username"));

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();

                    return chain.filter(mutatedExchange);
                })
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
