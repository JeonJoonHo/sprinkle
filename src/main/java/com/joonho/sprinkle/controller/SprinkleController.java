package com.joonho.sprinkle.controller;


import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.controller.response.LookupSprinkleResponse;
import com.joonho.sprinkle.controller.response.ReceiveSprinkleResponse;
import com.joonho.sprinkle.service.CreateSprinkleService;
import com.joonho.sprinkle.service.ReceiveSprinkleService;
import com.joonho.sprinkle.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SprinkleController {

    private final CreateSprinkleService createSprinkleService;
    private final ReceiveSprinkleService receiveSprinkleService;
    private final SprinkleService sprinkleService;

    @PostMapping("/api/sprinkles")
    ResponseEntity<CreateSprinkleResponse> create(
            @RequestBody CreateSprinkleRequest createSprinkleRequest,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        CreateSprinkleResponse response = createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/sprinkles/{token}/receive")
    ResponseEntity<ReceiveSprinkleResponse> receive(
            @PathVariable("token") String token,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        ReceiveSprinkleResponse response = receiveSprinkleService.receive(token, roomId, userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/sprinkles/{token}")
    ResponseEntity<LookupSprinkleResponse> lookup(
            @PathVariable("token") String token,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        LookupSprinkleResponse response = sprinkleService.lookup(token, userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
