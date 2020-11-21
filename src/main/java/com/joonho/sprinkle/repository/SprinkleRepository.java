package com.joonho.sprinkle.repository;

import com.joonho.sprinkle.model.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinkleRepository extends JpaRepository<Sprinkle, Long> {
}
