package com.joonho.sprinkle.repository;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprinkleTargetRepository extends JpaRepository<SprinkleTarget, Long> {
    Boolean existsBySprinkleAndReceiver(Sprinkle sprinkle, Long receiver);
    List<SprinkleTarget> findAllBySprinkleAndReceiverIsNotNull(Sprinkle sprinkle);
}
