package com.en.athena.services;

import com.en.athena.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // Maximum requests allowed per window
    private final int REQUEST_LIMIT = 3;

    // Window size in milliseconds
    private final long WINDOW_SIZE = 20 * 1000;

    private final Map<Long, Window> userRateLimitMap = new HashMap<>();

    public boolean isAllowed(Long userId) {
        long currentTime = Instant.now().toEpochMilli();
        Window window = userRateLimitMap.getOrDefault(userId, new Window(0, currentTime));



        // Check if the current time has exceeded the window size
        if (currentTime - window.startTime >= WINDOW_SIZE) {
            // Reset the window
            window.count = 1;
            window.startTime = currentTime;
            userRateLimitMap.put(userId, window);
            return true;
        }

        // Check if the request count exceeds the limit
        if (window.count < REQUEST_LIMIT) {
            window.count++;
            userRateLimitMap.put(userId, window);
            return true;
        }

        // Reject the request
        return false;
    }

    // Inner class to represent the window for each user
    private static class Window {
        int count; // Number of requests
        long startTime; // Start time of the window

        public Window(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
