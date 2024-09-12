package com.team11.user_service.appication.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomPreAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        log.debug("Request URI: {}", uri);  // 로그 추가

        if ("/api/users/login".equals(uri)) {
            log.debug("Skipping authentication for /api/users/login");
            filterChain.doFilter(request, response);
            return;
        }
        // 헤더에서 사용자 정보와 역할(Role)을 추출
        String username = request.getHeader("X-User-Name");
        String rolesHeader = request.getHeader("X-User-Roles");

        System.out.println("X-User-Name = " + username);
        System.out.println("X-User-Roles = " + rolesHeader);

        if (username != null && rolesHeader != null) {
            // rolesHeader에 저장된 역할을 SimpleGrantedAuthority로 변환
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.trim()))
                    .collect(Collectors.toList());

            // 사용자 정보를 기반으로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 헤더가 없거나 역할 정보가 없으면 401 응답
            return;
        }

        // 필터 체인을 계속해서 진행
        filterChain.doFilter(request, response);
    }
}
