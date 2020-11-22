package com.joonho.sprinkle.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "sprinkle_targets",
        indexes = {
                @Index(name = "idx_sprinkle_targets_on_sprinkle_id", columnList = "sprinkle_id"),
                @Index(name = "idx_sprinkle_targets_on_sprinkle_id_receiver", columnList = "sprinkle_id, receiver")
        }
)
@NoArgsConstructor
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
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public SprinkleTarget(Integer amount, Sprinkle sprinkle) {
        Assert.notNull(amount, "amount can not null");
        Assert.notNull(sprinkle, "sprinkle can not null");

        this.amount = amount;
        this.sprinkle = sprinkle;
    }

    public void updateReceiver(Long userId) {
        this.receiver = userId;
    }
}
