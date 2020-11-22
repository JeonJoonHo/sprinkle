package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSprinkleService {

    private final SprinkleTargetService sprinkleTargetService;
    private final SprinkleService sprinkleService;

    @Transactional
    public CreateSprinkleResponse sprinkle(CreateSprinkleRequest createSprinkleRequest, Long userId, String roomId) {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(userId)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        List<Integer> dividedAmounts = createSprinkleRequest.divideAmount();

        dividedAmounts.forEach ((dividedAmount) -> {
            SprinkleTarget sprinkleTarget = sprinkleTargetService.buildSprinkleTarget(sprinkle, dividedAmount);
            sprinkle.addTarget(sprinkleTarget);
        });

        sprinkleService.save(sprinkle);

        sprinkle.updateToken(UUID.randomUUID().toString().substring(0, 3));

        return new CreateSprinkleResponse(sprinkle.getToken());
    }
}
