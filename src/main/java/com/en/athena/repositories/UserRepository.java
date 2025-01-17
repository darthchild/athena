package com.en.athena.repositories;

import com.en.athena.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Custom query method
    boolean existsByUserId(Long userId);
}
