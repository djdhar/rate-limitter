package com.ratelimitter.limit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedWindowRateLimitterModel implements RateLimitterModel {

    @NonNull
    @JsonProperty("count")
    private Integer count;

    @NonNull
    @JsonProperty("start_time")
    private BigInteger startTime;

    @JsonProperty("is_allowed")
    private Boolean isAllowed;
}
