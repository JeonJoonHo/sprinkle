package com.joonho.sprinkle.controller.response;

import com.joonho.sprinkle.model.Sprinkle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LookupSprinkleResponse {

    private Integer totalAmount = 0;
    private Integer completeReceiveAmount = 0;
    private LocalDateTime createdAt;
    private List<LookupSprinkleTargetResponse> sprinkleTargets = new ArrayList<>();

    public LookupSprinkleResponse(Sprinkle sprinkle) {
        this.createdAt = sprinkle.getCreatedAt();
        this.totalAmount = 0;
        this.completeReceiveAmount = 0;

        sprinkle.getSprinkleTargets().forEach(sprinkleTarget -> {
            this.totalAmount += sprinkleTarget.getAmount();

            if (sprinkleTarget.getReceiver() != null) {
                this.completeReceiveAmount += sprinkleTarget.getAmount();
                this.sprinkleTargets.add(new LookupSprinkleTargetResponse(sprinkleTarget));
            }
        });
    }
}
