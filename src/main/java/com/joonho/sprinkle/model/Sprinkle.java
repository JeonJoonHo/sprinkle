package com.joonho.sprinkle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "sprinkles",
        indexes = {
                @Index(name = "unique_idx_sprinkles_on_token", columnList = "token", unique = true)
        }
)
@NoArgsConstructor
public class Sprinkle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String roomId;

    private Long sender;

    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "sprinkle", cascade = CascadeType.ALL)
    private List<SprinkleTarget> sprinkleTargets;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public Sprinkle(String roomId, Long sender, LocalDateTime expiredAt) {
        Assert.notNull(roomId, "room Id can not null");
        Assert.notNull(sender, "sender can not null");
        Assert.isTrue(expiredAt.isAfter(LocalDateTime.now()), "expiredAt must after current time");

        this.roomId = roomId;
        this.sender = sender;
        this.sprinkleTargets = new ArrayList<>();
        this.expiredAt = expiredAt;
    }


    public static final int CAN_LOOK_UP_DAY = 7;

    public void updateToken(String token) {
        this.token = token;
    }

    public void addTarget(SprinkleTarget sprinkleTarget) {
        this.sprinkleTargets.add(sprinkleTarget);
    }

    public Boolean isExpire(LocalDateTime targetDateTime) {
        return this.expiredAt.isBefore(targetDateTime);
    }

    public Boolean isOwner(Long userId) {
        return this.sender.equals(userId);
    }

    public Boolean isEqualRoom(String roomId) {
        return this.roomId.equals(roomId);
    }

    public Boolean isCanLookup(Long userId) {
        if (LocalDateTime.now().isAfter(this.createdAt.plusDays(CAN_LOOK_UP_DAY))) return false;

        return this.isOwner(userId);
    }
}
