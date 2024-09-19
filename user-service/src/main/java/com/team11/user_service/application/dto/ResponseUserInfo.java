package com.team11.user_service.application.dto;

import com.team11.user_service.domain.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ResponseUserInfo {

    private String username;
    private String nickname;
    private String email;
    private UUID slackId;
    private String role;
    private UUID hubId;

    public static ResponseUserInfo fromEntity(User user) {
        return ResponseUserInfo.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole().name())
                .hubId(user.getHubId())
                .build();
    }
}