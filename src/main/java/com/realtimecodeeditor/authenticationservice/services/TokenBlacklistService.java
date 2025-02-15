package com.realtimecodeeditor.authenticationservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.security.jwt.expiration}")
    private long EXPIRATION_TIME; // 24 hours in seconds

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "blacklisted", EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
