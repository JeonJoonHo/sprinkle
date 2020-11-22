package com.joonho.sprinkle.service;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
public class SprinkleTargetServiceTest {

    @InjectMocks
    @Spy
    private SprinkleTargetService sprinkleTargetService;

    private Sprinkle sprinkle = Sprinkle.builder()
            .sender(1L)
            .roomId("room")
            .expiredAt(LocalDateTime.now().plusMinutes(10))
            .build();

    @Test void Sprinkle_객체와_연결된_SprikleTarget_객체를_반환한다() {

        SprinkleTarget sprinkleTarget= sprinkleTargetService.buildSprinkleTarget(sprinkle, 1000);

        assertThat(sprinkleTarget.getSprinkle()).isEqualTo(sprinkle);
    }

    @Test void 할당이_가능한_Target_목록_중_첫_Target에_receiver를_할당한다() {
        Long expectedUserId = 1L;
        SprinkleTarget expectedSprinkleTarget = SprinkleTarget.builder().amount(1000).sprinkle(sprinkle).build();

        List<SprinkleTarget> sprinkleTargets = List.of(
                expectedSprinkleTarget,
                SprinkleTarget.builder().amount(2000).sprinkle(sprinkle).build()
        );

        doReturn(sprinkleTargets).when(sprinkleTargetService).findAllBySprinkleAndReceiverIsNull(sprinkle);

        sprinkleTargetService.allocateTarget(sprinkle, expectedUserId);

        assertThat(expectedSprinkleTarget.getReceiver()).isEqualTo(expectedUserId);
    }

    @Test void 할당이_가능한_Target_목록이_없으면_0을_반환한다() {
        List<SprinkleTarget> sprinkleTargets = List.nil();

        doReturn(sprinkleTargets).when(sprinkleTargetService).findAllBySprinkleAndReceiverIsNull(sprinkle);

        sprinkleTargetService.allocateTarget(sprinkle, 1L);

        assertThat(sprinkleTargetService.allocateTarget(sprinkle, 1L)).isEqualTo(0);
    }

    @Test void 할당의_모든_프로세스가_종료되면_amount를_반환한다() {
        Long expectedUserId = 1L;
        SprinkleTarget expectedSprinkleTarget = SprinkleTarget.builder().amount(1000).sprinkle(sprinkle).build();

        List<SprinkleTarget> sprinkleTargets = List.of(
                expectedSprinkleTarget,
                SprinkleTarget.builder().amount(2000).sprinkle(sprinkle).build()
        );

        doReturn(sprinkleTargets).when(sprinkleTargetService).findAllBySprinkleAndReceiverIsNull(sprinkle);

        assertThat(sprinkleTargetService.allocateTarget(sprinkle, expectedUserId)).isEqualTo(expectedSprinkleTarget.getAmount());
    }
}
