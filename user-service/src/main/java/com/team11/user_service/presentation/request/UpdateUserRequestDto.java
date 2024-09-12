package com.team11.user_service.presentation.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 적어주세요")
    private String username;

    private String nickname;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,15}$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 입력해주세요 ")
    private String password;

    private String email;

    private String slackId;

    private String role;
}
