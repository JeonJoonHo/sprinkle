package com.joonho.sprinkle.controller;


import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.service.CreateSprinkleService;
import com.joonho.sprinkle.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SprinkleController {

    private final CreateSprinkleService createSprinkleService;

    @PostMapping("/api/sprinkles")
    ResponseEntity<CreateSprinkleResponse> create(
            @RequestBody CreateSprinkleRequest createSprinkleRequest,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        CreateSprinkleResponse response = createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
