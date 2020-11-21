package com.joonho.sprinkle.repository;

import com.joonho.sprinkle.model.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SprinkleRepository extends JpaRepository<Sprinkle, Long> {
    Optional<Sprinkle> findByToken(String token);
}
