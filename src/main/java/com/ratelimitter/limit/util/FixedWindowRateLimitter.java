package com.ratelimitter.limit.util;

import org.springframework.stereotype.Component;

@Component
public class FixedWindowRateLimitter implements RateLimitter {
    @Override
    public boolean checkRateLimitterApproval(String userId) {
        return true;
    }
}
