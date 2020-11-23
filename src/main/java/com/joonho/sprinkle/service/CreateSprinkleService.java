package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSprinkleService {

    private final SprinkleTargetService sprinkleTargetService;
    private final SprinkleService sprinkleService;

    private static final char[] SPECIAL_CHARACTERS = { '~', '`', '!', '@', '#',
            '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', '{',
            ']', '}', '\\', '|', ';', ':', '\'', '"', ',', '<', '.', '>', '/',
            '?' };

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

        sprinkle.updateToken(generateUniqueUUID());

        return new CreateSprinkleResponse(sprinkle.getToken());
    }

    private String generateUniqueUUID() {
        String uniqueToken = UUID.randomUUID().toString().substring(0, 3);

        int retryCount = 0;
        while (retryCount < 3){
            if (!sprinkleService.existsByToken(uniqueToken)) break;

            retryCount++;
            uniqueToken = UUID.randomUUID().toString().substring(0, 3);
        }

        if (retryCount == 3) uniqueToken = uniqueToken.substring(0, 2) + getRandomSpecialChracter();

        return uniqueToken;
    }

    private char getRandomSpecialChracter() {
        SecureRandom rand = new SecureRandom();

        return SPECIAL_CHARACTERS[rand.nextInt(SPECIAL_CHARACTERS.length)];
    }
}
