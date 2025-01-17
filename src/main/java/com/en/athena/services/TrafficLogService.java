package com.en.athena.services;

import com.en.athena.entities.TrafficLog;
import com.en.athena.repositories.TrafficLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TrafficLogService {

    @Autowired
    private TrafficLogRepository trafficLogRepository;

    public void logRequest(String requestId,
                           Long userId,
                           String httpMethod,
                           String endpoint,
                           String userTier,
                           String userAgent,
                           String ipAddress
                           ) {

        TrafficLog log = new TrafficLog();
        log.setRequestId(requestId);
        log.setUserId(userId);
        log.setHttpMethod(httpMethod);
        log.setEndpoint(endpoint);
        log.setUserTier(userTier);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setTimestamp(LocalDateTime.now());

        trafficLogRepository.save(log);
    }
}

