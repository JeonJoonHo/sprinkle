package com.joonho.sprinkle.service;

import com.joonho.sprinkle.controller.response.LookupSprinkleResponse;
import com.joonho.sprinkle.exception.BadRequestException;
import com.joonho.sprinkle.exception.NotFoundException;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.repository.SprinkleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SprinkleServiceTest {

    @InjectMocks
    @Spy
    private SprinkleService sprinkleService;

    @Mock
    private SprinkleRepository sprinkleRepository;

    @Test void Token으로_조회를_성공하면_Sprinkle을_반환한다() {
        String token = "token";
        Sprinkle mockSprinkle = Sprinkle.builder().build();

        when(sprinkleRepository.findByToken(token)).thenReturn(Optional.of(mockSprinkle));

        assertThat(sprinkleService.findByToken(token)).isInstanceOf(Sprinkle.class);
    }

    @Test void Token으로_조회를_하지_못_하면_NotFoundException을_발생시킨다() {
        String token = "token";

        when(sprinkleRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> sprinkleService.findByToken(token))
                .withMessage("조회 할 수 없습니다.");
    }

    @Test void 생성일이_7일이_지난_Sprinkle을_조회하면_BadRequestException을_발생시킨다() {
        String token = "token";
        Long userId = 1L;
        Sprinkle mockSprinkle = Sprinkle.builder()
                .createdAt(LocalDateTime.now().minusDays(10))
                .sender(999L)
                .build();

        doReturn(mockSprinkle).when(sprinkleService).findByToken(token);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() ->
                sprinkleService.lookup(token, userId))
                .withMessage("조회 할 수 없습니다.");
    }

    @Test void 자신의_Sprinkle을_조회하면_BadRequestException을_발생시킨다() {
        String token = "token";
        Long userId = 1L;
        Sprinkle mockSprinkle = Sprinkle.builder()
                .createdAt(LocalDateTime.now())
                .sender(userId)
                .build();

        doReturn(mockSprinkle).when(sprinkleService).findByToken(token);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() ->
                sprinkleService.lookup(token, userId))
                .withMessage("조회 할 수 없습니다.");
    }

    @Test void 조회가_가능한_Sprinkle을_조회하면_LookupSprinkleResponse를_반환한다() {
        String token = "token";
        Long userId = 1L;
        Sprinkle mockSprinkle = Sprinkle.builder()
                .createdAt(LocalDateTime.now())
                .sender(999L)
                .build();

        doReturn(mockSprinkle).when(sprinkleService).findByToken(token);

        assertThat(sprinkleService.lookup(token, userId)).isInstanceOf(LookupSprinkleResponse.class);

    }
}
