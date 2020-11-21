package com.joonho.sprinkle.creator;

import com.joonho.sprinkle.model.SprinkleTarget;
import org.springframework.stereotype.Component;

public class SprinkleTargetCreator {

    SprinkleTarget saveSprinkle() {

        return SprinkleTarget.builder().build();
    }
}
