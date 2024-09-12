package com.team11.user_service.appication.service;

import com.team11.user_service.domain.repository.UserRepository;
import com.team11.user_service.presentation.request.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public String signUp(SignUpRequestDto requestDto) {

        // username 중복 확인
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 등록된 회원입니다.");
        }

        // nickname 중복 확인
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }

        // email 중복 확인
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        userRepository.save(SignUpRequestDto.toEntity(requestDto, encodedPassword));

        return "회원가입에 성공했습니다.";
    }

//    // 로그인
//    public ResponseEntity<LoginResponseDto> login(LoginRequestDto requestDto,
//                                                  HttpServletRequest request,
//                                                  HttpServletResponse response) {
//
//        try {
//            Authentication authentication
//                    = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
//
//            String username = authentication.getName();
//
//            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
//            String role = user.getRole().name();
//
//            // 토큰 생성
//            String accessToken = jwtUtil.createJwt("access", username, role, 600000L);
//            String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);
//
//            // 리프레시 토큰 저장
//            RefreshToken redisToken = new RefreshToken(refreshToken, username);
//            refreshTokenRepository.save(redisToken);
//
//            // 응답 설정
//            LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
//            return ResponseEntity.ok(loginResponseDto);
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }


//    // 회원 정보 수정
//    @Transactional
//    public void updateUser(UpdateUserRequestDto requestDto) {
//
//
//    }
}
