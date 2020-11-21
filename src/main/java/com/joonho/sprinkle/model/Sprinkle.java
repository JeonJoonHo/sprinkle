package com.joonho.sprinkle.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Entity
@Table(
        name = "sprinkles",
        indexes = {
                @Index(name = "unique_idx_sprinkles_on_token", columnList = "token", unique = true)
        }
)
public class Sprinkle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String token = UUID.randomUUID().toString().substring(0, 3);

    private String roomId;

    private Long sender;

    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "sprinkle", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SprinkleTarget> sprinkleTargets = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addTarget(SprinkleTarget sprinkleTarget) {
        this.sprinkleTargets.add(sprinkleTarget);
    }

    public Boolean isExpire(LocalDateTime targetDateTime) {
        return this.expiredAt.isAfter(targetDateTime);
    }

    public Boolean isOwner(Long userId) {
        return this.sender.equals(userId);
    }

    public Boolean isEqualRoom(String roomId) {
        return this.roomId.equals(roomId);
    }

    public Boolean isCanLookup(Long userId) {
        if (this.createdAt.isBefore(LocalDateTime.now().minusDays(7))) return false;

        return this.isOwner(userId);
    }
}
