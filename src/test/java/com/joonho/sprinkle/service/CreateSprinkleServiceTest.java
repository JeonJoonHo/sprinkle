package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.request.CreateSprinkleRequest;
import com.joonho.sprinkle.controller.response.CreateSprinkleResponse;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateSprinkleServiceTest {

    @InjectMocks
    private CreateSprinkleService createSprinkleService;

    @Mock
    private SprinkleService sprinkleService;

    @Mock
    private SprinkleTargetService sprinkleTargetService;

    private Integer targetNumbers = 3;
    private Integer amount = 10000;
    private Long userId = 1L;
    private String roomId = "room";

    private CreateSprinkleRequest createSprinkleRequest = new CreateSprinkleRequest(amount, targetNumbers);

    @BeforeEach
    void setUp() {
        Sprinkle sprinkle = Sprinkle.builder().roomId("room").sender(1L).expiredAt(LocalDateTime.now().plusMinutes(10)).build();
        when(sprinkleTargetService.buildSprinkleTarget(any(Sprinkle.class), anyInt())).thenReturn(SprinkleTarget.builder().amount(1000).sprinkle(sprinkle).build());
    }

    @Test
    void Sprinkle_객체를_생성하기위해_Save_함수를_호출한다() {
        createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        verify(sprinkleService, times(1)).save(any(Sprinkle.class));
    }

    @Test
    void Sprinkle_객체의_토큰을_반환한다() {
        CreateSprinkleResponse response = createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        assertThat(response.getToken().length()).isEqualTo(3);
    }

    @Test
    void 뿌릴_인원만큼_Sprinkle_Target을_추가한다() {
        createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        verify(sprinkleTargetService, times(targetNumbers)).buildSprinkleTarget(any(Sprinkle.class), anyInt());
    }

    @Test
    void 뿌릴_금액을_뿌릴_인원만큼_분배한다() {
        createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        verify(sprinkleTargetService, times(2)).buildSprinkleTarget(any(Sprinkle.class), eq(3333));
        verify(sprinkleTargetService, times(1)).buildSprinkleTarget(any(Sprinkle.class), eq(3334));
    }

    @Test
    void 유니크한_토큰을_생성하기_위해_총_3번_검사를_진행하고_그럼에도_유니크하지_않다면_특수문자를_추가한다() {
        List<String> characters = Arrays.asList("~", "`", "!", "@", "#",
                "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", "[", "{",
                "]", "}", "\\", "|", ";", ":", "\"", "\"", ",", "<", ".", ">", "/",
                "?");

        when(sprinkleService.existsByToken(anyString())).thenReturn(true);

        CreateSprinkleResponse createSprinkleResponse = createSprinkleService.sprinkle(createSprinkleRequest, userId, roomId);

        verify(sprinkleService, times(3)).existsByToken(anyString());
        assertThat(characters.contains(createSprinkleResponse.getToken().substring(2, 3))).isTrue();
    }
}
