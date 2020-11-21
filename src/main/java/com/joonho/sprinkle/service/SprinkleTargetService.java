package com.joonho.sprinkle.service;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import org.springframework.stereotype.Service;

@Service
public class SprinkleTargetService {
    public SprinkleTarget buildSprinkleTarget(Sprinkle sprinkle, Integer dividedAmount) {

        return SprinkleTarget.builder().amount(dividedAmount).sprinkle(sprinkle).build();
    }
}
