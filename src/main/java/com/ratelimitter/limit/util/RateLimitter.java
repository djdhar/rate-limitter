package com.ratelimitter.limit.util;

public interface RateLimitter {

    boolean checkRateLimitterApproval(String userId);
}
