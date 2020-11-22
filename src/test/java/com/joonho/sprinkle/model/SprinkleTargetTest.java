package com.joonho.sprinkle.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SprinkleTargetTest {

    @Test void Builder의_sprinkle이_비어있으면_Exception을_발생시킨다() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                SprinkleTarget.builder()
                        .amount(1000)
                        .build())
                .withMessage("sprinkle can not null");
    }

    @Test void Builder의_amount가_비어있으면_Exception을_발생시킨다() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                SprinkleTarget.builder()
                        .sprinkle(Sprinkle.builder().sender(1L).roomId("room").expiredAt(LocalDateTime.now().plusMinutes(10)).build())
                        .build())
                .withMessage("amount can not null");
    }
}
