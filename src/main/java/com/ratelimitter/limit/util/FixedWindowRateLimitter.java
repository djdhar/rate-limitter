package com.ratelimitter.limit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratelimitter.limit.model.FixedWindowRateLimitterModel;
import com.ratelimitter.limit.model.RateLimitterModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Component
public class FixedWindowRateLimitter extends RateLimitter {

    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;
    public static int MAX_COUNT = 3;
    public static int WINDOW_SIZE = 5;
    public static String FIXED_WINDOW_PREFIX = "FixedWindow_";

    public FixedWindowRateLimitter(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public RateLimitterModel checkRateLimitterApproval(String userId) {
        String REDIS_KEY_USER_ID = FIXED_WINDOW_PREFIX + userId;
        FixedWindowRateLimitterModel fwModel = getRateLimitterFromRedis(redisTemplate,
                                                    REDIS_KEY_USER_ID, FixedWindowRateLimitterModel.class, objectMapper);
        Instant currentTime = Instant.now();
        if (Objects.nonNull(fwModel)) {
            if (Duration.between(convertEpochMilliToInstant(fwModel.getStartTime()), currentTime).getSeconds() < WINDOW_SIZE) {
                if (fwModel.getCount() < MAX_COUNT) {
                    fwModel.setCount(fwModel.getCount() + 1);
                    fwModel.setIsAllowed(true);
                } else {
                    fwModel.setIsAllowed(false);
                }
            } else {
                fwModel = createDefaultFixedWindowRateLimitterModel();
            }
        } else {
            fwModel = createDefaultFixedWindowRateLimitterModel();
        }
        putRateLimitterToRedis(redisTemplate, REDIS_KEY_USER_ID, fwModel, objectMapper);
        return fwModel;
    }

    private static FixedWindowRateLimitterModel createDefaultFixedWindowRateLimitterModel() {
        return new FixedWindowRateLimitterModel(1, convertInstantToEpochMilli(Instant.now()), true);
    }
}
