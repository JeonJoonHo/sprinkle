package com.joonho.sprinkle.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(
        name = "sprinkle_targets",
        indexes = {
                @Index(name = "idx_sprinkle_targets_on_sprinkle_id", columnList = "sprinkle_id"),
                @Index(name = "idx_sprinkle_targets_on_sprinkle_id_receiver", columnList = "sprinkle_id, receiver")
        }
)
public class SprinkleTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer amount;

    private Long receiver;

    @ManyToOne
    @JoinColumn(name="sprinkle_id", referencedColumnName = "id")
    private Sprinkle sprinkle;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateReceiver(Long userId) {
        this.receiver = userId;
    }
}
