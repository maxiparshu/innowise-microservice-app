package by.innowise.course.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                return unauthorized(exchange);
            }

            try {
                String token = authHeader.substring(BEARER_PREFIX.length());

                Claims claims = jwtUtil.validate(token);

                Object userId = claims.get("userId");
                Object role = claims.get("role");

                if (userId == null || role == null) {
                    log.warn("JWT does not contain required claims");
                    return unauthorized(exchange);
                }

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(request -> request.headers(headers -> {
                            headers.set(USER_ID_HEADER, userId.toString());
                            headers.set(USER_ROLE_HEADER, role.toString());
                        }))
                        .build();

                return chain.filter(mutatedExchange);

            } catch (JwtException e) {
                log.warn("JWT validation failed: {}", e.getMessage());
                return unauthorized(exchange);
            } catch (Exception e) {
                log.error("Unexpected authentication error", e);
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}