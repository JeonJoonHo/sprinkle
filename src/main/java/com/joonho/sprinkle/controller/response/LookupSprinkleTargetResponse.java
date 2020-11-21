package com.joonho.sprinkle.controller.response;

import com.joonho.sprinkle.model.SprinkleTarget;

public class LookupSprinkleTargetResponse {

    private Integer receiveAmount;
    private Long receiver;

    public LookupSprinkleTargetResponse(SprinkleTarget sprinkleTarget) {
        this.receiveAmount = sprinkleTarget.getAmount();
        this.receiver = sprinkleTarget.getReceiver();
    }

}
