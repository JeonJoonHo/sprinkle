package com.joonho.sprinkle.controller.response;

import com.joonho.sprinkle.model.Sprinkle;

import java.time.LocalDateTime;
import java.util.List;

public class LookupSprinkleResponse {

    private Integer totalAmount;
    private Integer completeReceiveAmount;
    private LocalDateTime createdAt;
    private List<LookupSprinkleTargetResponse> sprinkleTargets;

    public LookupSprinkleResponse(Sprinkle sprinkle) {
        this.createdAt = sprinkle.getCreatedAt();

        sprinkle.getSprinkleTargets().forEach(sprinkleTarget -> {
            this.totalAmount += sprinkleTarget.getAmount();

            if (sprinkleTarget.getReceiver() != null) {
                this.completeReceiveAmount += sprinkleTarget.getAmount();
                this.sprinkleTargets.add(new LookupSprinkleTargetResponse(sprinkleTarget));
            }
        });
    }
}
