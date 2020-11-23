package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.response.ReceiveSprinkleResponse;
import com.joonho.sprinkle.exception.BadRequestException;
import com.joonho.sprinkle.model.Sprinkle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiveSprinkleServiceTest {

    @InjectMocks
    private ReceiveSprinkleService receiveSprinkleService;

    @Mock
    private SprinkleService sprinkleService;

    @Mock
    private SprinkleTargetService sprinkleTargetService;

    private String token = "token";
    private String roomId = "room";
    private Long userId = 1L;

    @Test void 모든_프로세스가_종료되면_ReceiveSprinkleResponse를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);
        when(sprinkleTargetService.allocateTargetWithRedisLock(sprinkle, userId)).thenReturn(1000);

        assertThat(receiveSprinkleService.receive(token, roomId, userId)).isInstanceOf(ReceiveSprinkleResponse.class);
    }

    @Test void 지급하기_위해_Sprinkle_Target을_할당하는_함수를_호출한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);
        when(sprinkleTargetService.allocateTargetWithRedisLock(sprinkle, userId)).thenReturn(1000);

        receiveSprinkleService.receive(token, roomId, userId);

        verify(sprinkleTargetService, times(1)).allocateTargetWithRedisLock(sprinkle, userId);
    }

    @Test void 시간이_만료된_뿌리기에_접근하면_Exception을_발생시킨다() throws NoSuchFieldException, IllegalAccessException {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        Field expiredAtField = sprinkle.getClass().getDeclaredField("expiredAt");
        expiredAtField.setAccessible(true);

        expiredAtField.set(sprinkle, LocalDateTime.now().minusDays(10));

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> receiveSprinkleService.receive(token, roomId, userId))
                .withMessage("뿌리기가 종료되었습니다.");
        verify(sprinkleTargetService, never()).allocateTargetWithRedisLock(sprinkle, userId);
    }

    @Test void 뿌리기를_보낸_사람이면_Exception을_발생시킨다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(userId)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> receiveSprinkleService.receive(token, roomId, userId))
                .withMessage("본인은 받을 수 없습니다.");
        verify(sprinkleTargetService, never()).allocateTargetWithRedisLock(sprinkle, userId);
    }

    @Test void 뿌리기를_보낸_채팅방이_아니면_Exception을_발생시킨다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId("another_room")
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> receiveSprinkleService.receive(token, roomId, userId))
                .withMessage("잘 못된 접근입니다.");
        verify(sprinkleTargetService, never()).allocateTargetWithRedisLock(sprinkle, userId);
    }

    @Test void 이미_지급_받은_내역이_있으면_Exception을_발생시킨다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);
        when(sprinkleTargetService.existsBySprinkleAndReceiver(sprinkle, userId)).thenReturn(true);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> receiveSprinkleService.receive(token, roomId, userId))
                .withMessage("이미 지급 받았습니다.");
        verify(sprinkleTargetService, never()).allocateTargetWithRedisLock(sprinkle, userId);
    }

    @Test void 할당_함수에서_0을_반환받으면_Exception을_발생시킨다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .roomId(roomId)
                .sender(100L)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(sprinkleService.findByToken(token)).thenReturn(sprinkle);
        when(sprinkleTargetService.allocateTargetWithRedisLock(sprinkle, userId)).thenReturn(0);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> receiveSprinkleService.receive(token, roomId, userId))
                .withMessage("뿌리기가 종료되었습니다.");
    }
}
