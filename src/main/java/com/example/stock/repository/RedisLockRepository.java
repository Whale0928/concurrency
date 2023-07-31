package com.example.stock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long key) {
        return redisTemplate
                // 레디스 키 값에 대한 연산을 수행하는 인터페이스
                .opsForValue()

                // 키 값이 존재하지 않으면 키 값을 생성하고 true를 반환
                // 키 , 값 , 만료 시간
                .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(Long key) {
        // 키 값이 존재하면 키 값을 삭제하고 true를 반환
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
