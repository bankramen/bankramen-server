package org.example.bankramenserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StateService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "oauth_state:";

    @Value("${oauth.state.expiration}")
    private long expiration;

    public String generateState() {
        String state = UUID.randomUUID().toString();
        redisTemplate.opsForValue()
                .set(PREFIX + state, state, expiration, TimeUnit.SECONDS);
        return state;
    }

    public boolean validateState(String state) {
        String saved = redisTemplate.opsForValue().get(PREFIX + state);
        if (saved == null) return false;
        redisTemplate.delete(PREFIX + state);
        return true;
    }
}
