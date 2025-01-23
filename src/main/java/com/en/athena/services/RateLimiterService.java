package com.en.athena.services;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final Map<Long, Bucket> userBuckets = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;

    public RateLimiterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Calls Flask API
    public Map<String, Object> fetchRateLimitConfig() {
        String url = "http://localhost:5000/get-ratelimit";

        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            // Log the error
            System.err.println("Error fetching rate limit from external API: " + e.getMessage());

            // Fallback to static rate limits
            return Map.of(
                    "capacity", 3,
                    "refillTokens", 1,
                    "refillDuration", 10
            );
        }
    }

    // Create a new bucket
    private Bucket createNewBucket(long capacity, long refillTokens, Duration refillDuration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(refillTokens, refillDuration));
        return Bucket4j.builder().addLimit(limit).build();
    }

    // Fetch or create new bucket for given userId
    public Bucket resolveBucket(Long userId) {
        Map<String, Object> config = fetchRateLimitConfig();
        int capacity = (int) config.get("capacity");
        int refillTokens = (int) config.get("refillTokens");
        int refillDuration = (int) config.get("refillDuration");

        return userBuckets.computeIfAbsent(userId, id ->
                createNewBucket(capacity, refillTokens, Duration.ofSeconds(refillDuration))
        );
    }

    // Check if the request is allowed
    public boolean isAllowed(Long userId) {
        Bucket bucket = resolveBucket(userId);
        return bucket.tryConsume(1);
    }
}
