package com.ratelimitter.limit.controller;

import com.ratelimitter.limit.model.RateLimitterModel;
import com.ratelimitter.limit.service.SampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SampleController {

    SampleService sampleService;
    SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("/sample/api")
    public RateLimitterModel sampleGet(@RequestHeader Map<String, String> headers) {
        return sampleService.sampleGetExecute(headers);
    }
}
