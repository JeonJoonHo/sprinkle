package com.joonho.sprinkle.service;

import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import com.joonho.sprinkle.repository.SprinkleTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinkleTargetService {

    private final SprinkleTargetRepository sprinkleTargetRepository;

    SprinkleTarget buildSprinkleTarget(Sprinkle sprinkle, Integer dividedAmount) {

        return SprinkleTarget.builder().amount(dividedAmount).sprinkle(sprinkle).build();
    }

    Boolean existsBySprinkleAndReceiver(Sprinkle sprinkle, Long receiver) {
        return sprinkleTargetRepository.existsBySprinkleAndReceiver(sprinkle, receiver);
    }

    @Transactional
    Integer allocateTarget(Sprinkle sprinkle, Long userId) {
        List<SprinkleTarget> sprinkleTargets = findAllBySprinkleAndReceiverIsNotNull(sprinkle);

        SprinkleTarget selectedSprinkleTarget = sprinkleTargets.get(0);
        selectedSprinkleTarget.updateReceiver(userId);

        return selectedSprinkleTarget.getAmount();
    }

    private List<SprinkleTarget> findAllBySprinkleAndReceiverIsNotNull(Sprinkle sprinkle) {
        return sprinkleTargetRepository.findAllBySprinkleAndReceiverIsNotNull(sprinkle);
    }
}
