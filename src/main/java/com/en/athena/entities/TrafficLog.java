package com.en.athena.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "http_method")
    private String httpMethod;

    private String endpoint;

    @Column(name = "user_tier")
    private String userTier;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    private LocalDateTime timestamp;
}

