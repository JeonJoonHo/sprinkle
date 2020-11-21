package com.joonho.sprinkle.repository;

import com.joonho.sprinkle.model.SprinkleTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinkleTargetRepository extends JpaRepository<SprinkleTarget, Long> {
}
