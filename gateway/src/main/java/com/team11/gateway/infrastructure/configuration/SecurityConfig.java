package com.team11.gateway.infrastructure.configuration;

import com.team11.gateway.application.dto.UserDto;
import com.team11.gateway.application.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    private final RedisService redisService;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(jwtAuthenticationFilter(redisService), SecurityWebFiltersOrder.HTTP_BASIC); // csrf 비활성화

        return http.build();
    }

    @Bean
    public WebFilter jwtAuthenticationFilter(RedisService redisService) {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            log.debug("Request Path: {}", path);

            // 로그 추가: Authorization 헤더 확인
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.debug("Authorization header: {}", authorizationHeader);

            // /api/users/login과 /api/users/signup 경로는 필터를 적용하지 않음
            if ("/api/users/login".equals(path) || "/api/users/signup".equals(path)) {
                log.debug("Skipping filter for path: {}", path);
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            log.info("Authorization header: {}", authHeader); // 로그 추가

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                log.info("Extracted token: {}", token); // 로그 추가

                try {
                    byte[] bytes = Base64.getDecoder().decode(secretKey);
                    var key = Keys.hmacShaKeyFor(bytes);

                    log.info("Decoded secret key bytes length: {}", bytes.length); // 로그 추가

                    Claims claims = Jwts
                            .parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    log.info("Claims: {}", claims); // 로그 추가

                    String username = claims.getSubject();
                    log.info("Username from claims: {}", username); // 로그 추가

                    var key1 = "user:" + username;
                    var userDto = redisService.getValueAsClass(key1, UserDto.class);

                    if (userDto == null) {
                        log.error("No user data found for key: {}", key);
                    } else {
                        log.info("User data retrieved: {}", userDto);
                    }

                    var finalUserDto = Optional.ofNullable(
                            redisService.getValueAsClass("user:" + username, UserDto.class)
                    ).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

                    // 사용자 정보를 새로운 헤더에 추가
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Name", username)
                            .header("X-User-Roles", String.join(",", userDto.getRoles()))
                            .build();

                    // 수정된 요청으로 필터 체인 계속 처리
                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                    return chain.filter(modifiedExchange);

                } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
                    log.error("JWT validation error: {}", e.getMessage(), e); // 로그 추가
                    return Mono.error(new RuntimeException("Invalid JWT Token"));
                }
            }

            return chain.filter(exchange);
        };
    }
}