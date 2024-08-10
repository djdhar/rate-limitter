package com.ratelimitter.limit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratelimitter.limit.model.RateLimitterModel;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigInteger;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Objects;

public abstract class RateLimitter {

    public abstract RateLimitterModel checkRateLimitterApproval(String userId);

    protected static LinkedHashMap convertObjectToLinkedHashMap(Object obj, ObjectMapper objectMapper) {
        if (Objects.isNull(obj)) return new LinkedHashMap();
        return objectMapper.convertValue(obj, LinkedHashMap.class);
    }

    protected static <T> T convertLinkedHashMapToObject(LinkedHashMap<String, Object> map, Class<T> clazz, ObjectMapper objectMapper) {
        if (Objects.isNull(map) || map.isEmpty()) return null;
        return objectMapper.convertValue(map, clazz);
    }

    protected static Instant convertEpochMilliToInstant(BigInteger timeEpochMilli) {
        if(Objects.isNull(timeEpochMilli)) return null;
        return Instant.ofEpochMilli(timeEpochMilli.longValue());
    }

    protected static BigInteger convertInstantToEpochMilli(Instant instant) {
        if(Objects.isNull(instant)) return null;
        return BigInteger.valueOf(instant.toEpochMilli());
    }

    protected static void putRateLimitterToRedis(RedisTemplate redisTemplate, String key, RateLimitterModel limitterModel, ObjectMapper objectMapper) {
        redisTemplate.opsForValue().set(key, convertObjectToLinkedHashMap(limitterModel, objectMapper));
    }

    protected static <T> T getRateLimitterFromRedis(RedisTemplate redisTemplate, String key, Class<T> clazz, ObjectMapper objectMapper) {
        return convertLinkedHashMapToObject(
                (LinkedHashMap<String, Object>) redisTemplate.opsForValue().get(key),
                clazz, objectMapper);
    }
}
