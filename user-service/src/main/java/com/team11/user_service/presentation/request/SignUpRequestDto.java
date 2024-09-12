package com.team11.user_service.presentation.request;

import com.team11.user_service.domain.model.User;
import com.team11.user_service.domain.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank(message = "아이디를 입력해 주세요")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 적어주세요")
    private String username;

    @NotBlank(message = "닉네임을 입력해 주세요")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,15}$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 입력해주세요 ")
    private String password;

    @NotBlank(message = "이메일을 입력해 주세요")
    private String email;

    private String slackId;

    @NotBlank(message = "권한을 입력해 주세요")
    private String role;

    // 나중에 password encoder 처리해야함
    public static User toEntity(SignUpRequestDto dto , String encodedPassword) {
        return User.builder()
                .username(dto.username)
                .nickname(dto.nickname)
                .password(encodedPassword)
                .email(dto.email)
                .slackId(dto.slackId)
                .role(UserRole.valueOf(dto.role))
                .build();
    }

}
