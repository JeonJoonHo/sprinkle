package com.joonho.sprinkle.controller.request;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CreateSprinkleRequest {
    private Integer amount;
    private Integer targetNumbers;

    public List<Integer> divideAmount() {
        Integer divideAmount = (int)Math.floor(this.amount / this.targetNumbers);
        Integer currentTotalAmount = 0;

        List<Integer> dividedAmounts = new ArrayList<>();

        for (int number = 0; number < this.targetNumbers-1; number++) {
            dividedAmounts.add(divideAmount);
            currentTotalAmount += divideAmount;
        }

        dividedAmounts.add(this.amount - currentTotalAmount);

        return dividedAmounts;
    }
}
