package com.team11.user_service.appication.service;

import com.team11.user_service.appication.dto.MessageResponseDto;
import com.team11.user_service.domain.model.User;
import com.team11.user_service.domain.repository.UserRepository;
import com.team11.user_service.presentation.request.SignUpRequestDto;
import com.team11.user_service.presentation.request.UpdateUserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    // 회원 정보 수정
    public MessageResponseDto updateUser(UpdateUserRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

//         회원 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다.")
        );

        System.out.println("user.getNickname() = " + user.getNickname());
        System.out.println("requestDto.getNickname() = " + requestDto.getNickname());
        
        // 닉네임 중복 확인
        if (requestDto.getNickname() != null && !requestDto.getNickname().equals(user.getNickname())) {
            Optional<User> byNickname = userRepository.findByNickname(requestDto.getNickname());
            if (byNickname.isPresent()) {
                throw new IllegalArgumentException("중복된 닉네임 입니다.");
            }
            user.updateNickname(requestDto.getNickname());
        }

        // 이메일 중복 확인
        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(user.getEmail())) {
            Optional<User> byEmail = userRepository.findByEmail(requestDto.getEmail());
            if (byEmail.isPresent()) {
                throw new IllegalArgumentException("중복된 이메일 입니다.");
            }
            user.updateEmail(requestDto.getEmail());
        }

        if (requestDto.getPassword() != null && !requestDto.getCheckPassword().isEmpty()) {
            // 기존 비밀번호 확인
            if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 확인
            if (!requestDto.getPassword().equals(requestDto.getCheckPassword())) {
                throw new IllegalArgumentException("비밀번호 확인란을 다시 입력해 주세요");
            }

            // 새 비밀번호 인코딩 및 저장
            user.updatePassword(bCryptPasswordEncoder.encode(requestDto.getPassword()));
        }

        user.updatedBy(user);
        userRepository.save(user);

        return new MessageResponseDto("회원 정보가 수정되었습니다.");
    }

}
