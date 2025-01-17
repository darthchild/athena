package com.en.athena.filters;

import com.en.athena.services.RateLimiterService;
import com.en.athena.services.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class RateLimiterFilter implements Filter {

    private final RateLimiterService rateLimiterService;
    private final UserService userService;

    public RateLimiterFilter(RateLimiterService rateLimiterService, UserService userService) {
        this.rateLimiterService = rateLimiterService;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        Long userId = Optional.ofNullable(httpRequest.getHeader("User-ID"))
                .map(id -> {
                    try {
                        return Long.parseLong(id);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("User-ID header is invalid. Must be a valid number.", e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("User-ID header is missing or invalid."));




        // Exclude H2 console from rate limiting
        if (requestURI.contains("/h2-console")) {
            chain.doFilter(request, response); // Skip rate limiting
            return;
        }

        // Check if the User exists in the DB
        if(!userService.doesUserExist(userId)){
            httpResponse.setStatus(404); // HTTP 429 Too Many Requests
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{ " +
                    "\"status\" : 404," +
                    "\"error\": \"Not Found\"," +
                    "\"message\" : \"This User ID doesn't exist in the database.\""+
                    "}");
            return ;
        } else {
            System.out.println(userId + " : User ID validated");
        }

        // Rate Limiting
        if (!rateLimiterService.isAllowed(userId)) {

            // Handle rate limit exceeded
            httpResponse.setStatus(429); // HTTP 429 Too Many Requests
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{ " +
                    "\"status\" : 429," +
                    "\"error\": \"Too Many Requests\"," +
                    "\"message\" : \"Rate limit exceeded. Try again later.\""+
                    "}");
            return; // Exit the filter chain
        }

        // Pass request to the next filter / controller
        chain.doFilter(request, response);
    }
}

