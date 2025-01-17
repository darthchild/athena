package com.en.athena.filters;

import com.en.athena.services.TrafficLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class TrafficLogFilter implements Filter {

    private final TrafficLogService trafficLogService;

    public TrafficLogFilter(TrafficLogService trafficLogService) {
        this.trafficLogService = trafficLogService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestId = UUID.randomUUID().toString();
        String userTier = httpRequest.getHeader("User-Tier");
        String httpMethod = httpRequest.getMethod();
        String endpoint = httpRequest.getRequestURI();
        String ipAddress = request.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        Long userId = Optional.ofNullable(httpRequest.getHeader("User-ID"))
                .map(id -> {
                    try {
                        return Long.parseLong(id);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("User-ID header is invalid. Must be a valid number.", e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("User-ID header is missing or invalid."));

        //        try {
        //            if (userIdHeader != null) {
        //                userId = Long.parseLong(userIdHeader);
        //            } else {
        //                throw new IllegalArgumentException("User-ID header is missing.");
        //            }
        //        } catch (NumberFormatException e) {
        //            throw new IllegalArgumentException("Invalid User-ID format. Must be a numeric value.", e);
        //        }


        // Exclude H2 console from Traffic Logging
        if (endpoint.contains("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }



        trafficLogService.logRequest(requestId, userId, httpMethod, endpoint, userTier, userAgent, ipAddress);

        System.out.println(userId);

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}

