package com.joonho.sprinkle.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SprinkleTest {

    @Test void Builder의_roomId가_비어있으면_Exception을_발생시킨다() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                Sprinkle.builder()
                        .sender(1L)
                        .expiredAt(LocalDateTime.now().plusMinutes(10))
                        .build())
                .withMessage("room Id can not null");
    }

    @Test void Builder의_sender가_비어있으면_Exception을_발생시킨다() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                Sprinkle.builder()
                        .roomId("room")
                        .expiredAt(LocalDateTime.now().plusMinutes(10))
                        .build())
                .withMessage("sender can not null");
    }

    @Test void Builder의_expiredAt이_현재_시간보다_과거이면_Exception을_발생시킨다() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                Sprinkle.builder()
                        .sender(1L)
                        .roomId("room")
                        .expiredAt(LocalDateTime.now().minusMinutes(10))
                        .build())
                .withMessage("expiredAt must after current time");
    }

    @Test void 파라미터의_일자가_expiredAt보다_초과했으면_true를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isExpire(LocalDateTime.now().plusMinutes(15))).isTrue();
    }

    @Test void 파라미터의_일자가_expiredAt보다_초과했으면_false를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isExpire(LocalDateTime.now())).isFalse();
    }

    @Test void 파라미터의_userId가_sender와_같으면_true를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isOwner(1L)).isTrue();
    }

    @Test void 파라미터의_userId가_sender와_다르면_false를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isOwner(9999L)).isFalse();
    }

    @Test void 파라미터의_roomId가_roomId와_같으면_true를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isEqualRoom("room")).isTrue();
    }

    @Test void 파라미터의_roomId가_roomId와_다르면_false를_반환한다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isEqualRoom("another_room")).isFalse();
    }

    @Test void 뿌리기의_보낸_사람이_아니면_lookUp_할_수_없다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isCanLookup(9999L)).isFalse();
    }

    @Test void 생성된지_7일_이_후_에는_lookUp_할_수_없다() throws NoSuchFieldException, IllegalAccessException {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        Field createdAtField = sprinkle.getClass().getDeclaredField("createdAt");
        createdAtField.setAccessible(true);

        createdAtField.set(sprinkle, LocalDateTime.now().minusDays(10));

        assertThat(sprinkle.isCanLookup(1L)).isFalse();
    }

    @Test void 생성된지_7일이_되지_않고_보낸_사람이면_lookUp이_가능하다() {
        Sprinkle sprinkle = Sprinkle.builder()
                .sender(1L)
                .roomId("room")
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        assertThat(sprinkle.isCanLookup(1L)).isTrue();
    }
}
