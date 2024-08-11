package com.ratelimitter.limit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigInteger;
import java.util.SortedSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlidingWindowRateLimitterModel implements RateLimitterModel {
    @NonNull
    @JsonProperty("request_instants")
    SortedSet<BigInteger> requestInstants;

    @JsonProperty("is_allowed")
    private Boolean isAllowed;
}
