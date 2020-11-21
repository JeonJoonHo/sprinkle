package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import com.joonho.sprinkle.repository.SprinkleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SprinkleServiceTest {

    @InjectMocks
    private SprinkleService sprinkleService;

    @Mock
    private SprinkleRepository sprinkleRepository;

    @Mock
    private SprinkleTargetService sprinkleTargetService;

    private Integer targetNumbers = 3;
    private Integer amount = 10000;
    private Long userId = 1L;
    private String roomId = "room";

    private CreateSprinkleRequest createSprinkleRequest = new CreateSprinkleRequest(amount, targetNumbers);


}
