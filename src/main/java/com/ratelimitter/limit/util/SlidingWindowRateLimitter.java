package com.ratelimitter.limit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratelimitter.limit.model.RateLimitterModel;
import com.ratelimitter.limit.model.SlidingWindowRateLimitterModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class SlidingWindowRateLimitter extends RateLimitter {

    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;
    public static int MAX_COUNT = 3;
    public static int WINDOW_SIZE = 5;
    public static String SLIDING_WINDOW_PREFIX = "SlidingWindow_";

    public SlidingWindowRateLimitter(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public RateLimitterModel checkRateLimitterApproval(String userId) {
        String REDIS_KEY_USER_ID = SLIDING_WINDOW_PREFIX + userId;
        SlidingWindowRateLimitterModel swModel = getRateLimitterFromRedis(redisTemplate,
                REDIS_KEY_USER_ID, SlidingWindowRateLimitterModel.class, objectMapper);
        boolean isAllowed = false;

        if (Objects.nonNull(swModel)) {
            Instant currentTime = Instant.now();
            if (Objects.nonNull(swModel.getRequestInstants())) {
                BigInteger currentTimeEpochMilli = convertInstantToEpochMilli(currentTime);
                BigInteger thresholdTimeEpochMilli = currentTimeEpochMilli.subtract( BigInteger.valueOf(WINDOW_SIZE* 1000L));
                SortedSet<BigInteger> requestInstants = swModel.getRequestInstants();
                requestInstants.headSet(thresholdTimeEpochMilli).clear();
                if (requestInstants.size() < MAX_COUNT) {
                    requestInstants.add(currentTimeEpochMilli);
                    isAllowed = true;
                }
                swModel.setRequestInstants(requestInstants);
                swModel.setIsAllowed(isAllowed);
            }
        } else {
            SortedSet<BigInteger> requestInstants = new TreeSet<>();
            requestInstants.add(convertInstantToEpochMilli(Instant.now()));
            swModel = new SlidingWindowRateLimitterModel(requestInstants, true);
        }
        putRateLimitterToRedis(redisTemplate, REDIS_KEY_USER_ID, swModel, objectMapper);
        return swModel;
    }
}
