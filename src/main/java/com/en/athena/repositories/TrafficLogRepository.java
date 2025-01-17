package com.en.athena.repositories;

import com.en.athena.entities.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficLogRepository extends JpaRepository<TrafficLog, Long> {
}
