package com.team11.user_service.presentation.controller;

import com.team11.user_service.appication.dto.MessageResponseDto;
import com.team11.user_service.appication.dto.ResponseUserInfo;
import com.team11.user_service.appication.jwt.JWTUtil;
import com.team11.user_service.appication.service.RedisService;
import com.team11.user_service.appication.service.UserService;
import com.team11.user_service.presentation.request.CustomUserDetails;
import com.team11.user_service.presentation.request.LoginRequestDto;
import com.team11.user_service.presentation.request.SignUpRequestDto;
import com.team11.user_service.presentation.request.UpdateUserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
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

    @Operation(summary = "회원 가입",description = "사용자가 회원 가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(requestDto));
    }

    @Operation(summary = "로그인", description = "회원가입한 사용자가 로그인을 합니다.")
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
//    @GetMapping("/manager")
//    @PreAuthorize("hasRole('MANAGER')")  // 'ADMIN' 역할을 가진 사용자만 접근 가능
//    public String getAdminData(HttpServletRequest request) {
//        String username = request.getHeader("X-User-Name");
//        String rolesHeader = request.getHeader("X-User-Roles");
//        System.out.println("username = " + username);
//        System.out.println("rolesHeader = " + rolesHeader);
//        // 이 메서드는 'ADMIN' 역할을 가진 사용자만 접근할 수 있습니다.
//        return "Admin data";
//    }

    @Operation(summary = "회원 정보 수정",description = "MANAGER,DRIVER,COMPANY 권한을 가진 회원만 회원 정보 수정이 가능합니다.")
    @PutMapping()
    @PreAuthorize("hasRole('MANANGER') or hasRole('DRIVER') or hasRole('COMPANY')")
    public ResponseEntity<MessageResponseDto> updateUser(@RequestBody UpdateUserRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(requestDto));
    }

    @Operation(summary = "회원 탈퇴",description = "사용자가 회원 탈퇴를 합니다.")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER') or hasRole('DRIVER') or hasRole('COMPANY') ")
    public ResponseEntity<MessageResponseDto> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }

    @Operation(summary = "슬랙Id 확인", description = "사용자의 슬랙Id를 확인합니다.")
    @GetMapping("/divers/{userId}")
    @PreAuthorize("hasRole('MASTER') or hasRole('MANAGER')")
    public UUID getSlackId(@PathVariable Long userId) {
        return userService.getSlackId(userId);
    }

    @Operation(summary = "사용자 정보 조회",description = "사용자의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ResponseUserInfo> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

    @Operation(summary = "사용자 정보 전체 조회", description = "사용자의 정보를 전체 조회 합니다.")
    @GetMapping
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<List<ResponseUserInfo>> getAllUserInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUserInfo());
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
