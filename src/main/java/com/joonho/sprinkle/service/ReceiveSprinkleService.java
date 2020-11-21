package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.response.ReceiveSprinkleResponse;
import com.joonho.sprinkle.model.Sprinkle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReceiveSprinkleService {

    private final SprinkleService sprinkleService;
    private final SprinkleTargetService sprinkleTargetService;

    public ReceiveSprinkleResponse receive(String token , String roomId, Long userId) {
        Sprinkle sprinkle = sprinkleService.findByToken(token);

        // TODO : 에러 정의 필요
        if (sprinkle.isExpire(LocalDateTime.now())) throw new RuntimeException();
        if (sprinkle.isOwner(userId)) throw new RuntimeException();
        if (sprinkle.isEqualRoom(roomId)) throw new RuntimeException();
        if (sprinkleTargetService.existsBySprinkleAndReceiver(sprinkle, userId)) throw new RuntimeException();

        Integer amount = sprinkleTargetService.allocateTarget(sprinkle, userId);

        return new ReceiveSprinkleResponse(amount);
    }
}
