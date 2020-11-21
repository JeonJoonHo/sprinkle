package com.joonho.sprinkle.service;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SprinkleService {

    private final SprinkleRepository sprinkleRepository;


    public Sprinkle save(Sprinkle sprinkle) {
        return sprinkleRepository.save(sprinkle);
    }
}
