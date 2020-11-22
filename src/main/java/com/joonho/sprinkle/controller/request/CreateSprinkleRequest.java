package com.joonho.sprinkle.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateSprinkleRequest {
    @NotNull(message = "amount must not amount")
    private Integer amount;

    @NotNull(message = "targetNumbers must not amount")
    private Integer targetNumbers;

    public CreateSprinkleRequest(Integer amount, Integer targetNumbers) {
        this.amount = amount;
        this.targetNumbers = targetNumbers;
    }

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
