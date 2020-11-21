package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.response.ReceiveSprinkleResponse;
import com.joonho.sprinkle.exception.BadRequestException;
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

        if (sprinkle.isExpire(LocalDateTime.now())) throw new BadRequestException(sprinkle.getId(), "뿌리기가 종료되었습니다.");
        if (sprinkle.isOwner(userId)) throw new BadRequestException(sprinkle.getId(), "본인은 받을 수 없습니다.");
        if (sprinkle.isEqualRoom(roomId)) throw new BadRequestException(sprinkle.getId(), "잘 못된 접근입니다.");
        if (sprinkleTargetService.existsBySprinkleAndReceiver(sprinkle, userId)) throw new BadRequestException(sprinkle.getId(), "이미 지급 받았습니다.");;

        Integer amount = sprinkleTargetService.allocateTarget(sprinkle, userId);

        return new ReceiveSprinkleResponse(amount);
    }
}
