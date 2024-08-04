package com.ratelimitter.limit.service;

import com.ratelimitter.limit.util.FixedWindowRateLimitter;
import com.ratelimitter.limit.util.RateLimitter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SampleService implements SampleServiceInterface {
    RateLimitter rateLimitter;
    public SampleService(RateLimitter rateLimitter) {
        this.rateLimitter = rateLimitter;
    }
    public Map<String, String> sampleGetExecute(Map<String, String> headers) {
        if (headers.containsKey("userid")
                && rateLimitter.checkRateLimitterApproval(headers.get("userid"))) {
            return Map.of("request", "success");
        } else {
            return Map.of("request", "failure");
        }
    }
}
