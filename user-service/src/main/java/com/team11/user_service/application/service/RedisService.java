package com.team11.user_service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 값을 redis에 저장하는 메서드
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

}