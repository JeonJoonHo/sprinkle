package com.joonho.sprinkle.service;

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
                .orElseThrow(() -> new NotFoundException(String.format("Sprinkle [token:%s] not found", token)));
    }

    public Sprinkle save(Sprinkle sprinkle) {
        return sprinkleRepository.save(sprinkle);
    }
}
