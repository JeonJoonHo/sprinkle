package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.response.LookupSprinkleResponse;
import com.joonho.sprinkle.exception.BadRequestException;
import com.joonho.sprinkle.exception.NotFoundException;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SprinkleService {

    private final SprinkleRepository sprinkleRepository;

    public Sprinkle findByToken(String token) {
        return sprinkleRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(token, "조회 할 수 없습니다."));
    }

    public LookupSprinkleResponse lookup(String token, Long userId) {
        Sprinkle sprinkle = findByToken(token);

        if (!sprinkle.isCanLookup(userId)) throw new BadRequestException(sprinkle.getId(), "조회 할 수 없습니다.");;

        return new LookupSprinkleResponse(sprinkle);
    }

    public Sprinkle save(Sprinkle sprinkle) {
        return sprinkleRepository.save(sprinkle);
    }
}
