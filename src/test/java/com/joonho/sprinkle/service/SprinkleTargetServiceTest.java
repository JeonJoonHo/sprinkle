package com.joonho.sprinkle.service;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class SprinkleTargetServiceTest {

    @InjectMocks
    private SprinkleTargetService sprinkleTargetService;

    private Sprinkle sprinkle = Sprinkle.builder().build();

    @Test void Sprinkle_객체와_연결된_SprikleTarget_객체를_반환한다() {
        SprinkleTarget sprinkleTarget= sprinkleTargetService.buildSprinkleTarget(sprinkle, 1000);

        assertThat(sprinkleTarget.getSprinkle()).isEqualTo(sprinkle);
    }
}
