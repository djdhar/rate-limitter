package com.ratelimitter.limit.service;

import com.ratelimitter.limit.model.RateLimitterModel;
import com.ratelimitter.limit.util.RateLimitter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SampleService implements SampleServiceInterface {
    RateLimitter rateLimitter;
    public SampleService(RateLimitter rateLimitter) {
        this.rateLimitter = rateLimitter;
    }
    public RateLimitterModel sampleGetExecute(Map<String, String> headers) {
        if (headers.containsKey("userid")) {
                RateLimitterModel limitter = rateLimitter.checkRateLimitterApproval(headers.get("userid"));
            return limitter;
        } else {
            return null;
        }
    }
}
