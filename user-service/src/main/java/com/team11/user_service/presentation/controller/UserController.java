package com.team11.user_service.presentation.controller;

import com.team11.user_service.appication.dto.MessageResponseDto;
import com.team11.user_service.appication.jwt.JWTUtil;
import com.team11.user_service.appication.service.RedisService;
import com.team11.user_service.appication.service.UserService;
import com.team11.user_service.presentation.request.CustomUserDetails;
import com.team11.user_service.presentation.request.LoginRequestDto;
import com.team11.user_service.presentation.request.SignUpRequestDto;
import com.team11.user_service.presentation.request.UpdateUserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final JWTUtil jwtUtil;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(requestDto));
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto requestDto) {
        // 인증 서버 로그인 시, 토큰 발급 처리

        // 사용자 인증 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );

        // 인증 성공 시, redis 저장을 위한 처리를 위해 user를 가져옴
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            log.error("Expected CustomUserDetails but found: " + principal.getClass().getName());
            throw new RuntimeException("Authentication returned unexpected principal");
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;

        Collection<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // userDto는 redis에 저장하기 위한 중간 dto
        var userDto = UserDto.fromUser(customUserDetails);

        redisService.setValue("user:" + requestDto.getUsername(), userDto);

        return jwtUtil.createJwt(customUserDetails.getUsername(), roles, 3600000L);
    }

    // 권한 확인
    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")  // 'ADMIN' 역할을 가진 사용자만 접근 가능
    public String getAdminData(HttpServletRequest request) {
        String username = request.getHeader("X-User-Name");
        String rolesHeader = request.getHeader("X-User-Roles");
        System.out.println("username = " + username);
        System.out.println("rolesHeader = " + rolesHeader);
        // 이 메서드는 'ADMIN' 역할을 가진 사용자만 접근할 수 있습니다.
        return "Admin data";
    }

    // 회원 정보 수정
    @PutMapping()
    @PreAuthorize("hasRole('MANANGER') or hasRole('DRIVER') or hasRole('COMPANY')")
    public ResponseEntity<MessageResponseDto> updateUser(@RequestBody UpdateUserRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(requestDto));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('DRIVER') or hasRole('COMPANY') ")
    public ResponseEntity<MessageResponseDto> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }

    private record UserDto(String username, Collection<String> roles) {
        public static UserDto fromUser(CustomUserDetails userDetails) {
            return new UserDto(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            );
        }
    }
}
