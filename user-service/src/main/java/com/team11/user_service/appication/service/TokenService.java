package com.team11.user_service.appication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken) {
        // 30일 동안 유효
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofDays(30));
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }

}
