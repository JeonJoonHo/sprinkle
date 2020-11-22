package com.joonho.sprinkle.service;

import com.joonho.sprinkle.exception.BadRequestException;
import com.joonho.sprinkle.model.Sprinkle;
import com.joonho.sprinkle.model.SprinkleTarget;
import com.joonho.sprinkle.repository.SprinkleTargetRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SprinkleTargetService {

    private final SprinkleTargetRepository sprinkleTargetRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public Integer allocateTarget(Sprinkle sprinkle, Long userId) {
        RLock lock = redissonClient.getLock("allocate_target_" + sprinkle.getToken());

        boolean res = false;
        try {
            res = lock.tryLock(60, 20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new BadRequestException(sprinkle.getId(), "다시 시도해주세요.");
        }

        if (!res) throw new BadRequestException(sprinkle.getId(), "다시 시도해주세요.");

        try {
            List<SprinkleTarget> sprinkleTargets = findAllBySprinkleAndReceiverIsNull(sprinkle);

            if (sprinkleTargets.isEmpty()) return 0;

            SprinkleTarget selectedSprinkleTarget = sprinkleTargets.get(0);
            selectedSprinkleTarget.updateReceiver(userId);

            return selectedSprinkleTarget.getAmount();
        }
        finally {
            lock.unlock();
        }
    }

    SprinkleTarget buildSprinkleTarget(Sprinkle sprinkle, Integer dividedAmount) {

        return SprinkleTarget.builder().amount(dividedAmount).sprinkle(sprinkle).build();
    }

    Boolean existsBySprinkleAndReceiver(Sprinkle sprinkle, Long receiver) {
        return sprinkleTargetRepository.existsBySprinkleAndReceiver(sprinkle, receiver);
    }

    List<SprinkleTarget> findAllBySprinkleAndReceiverIsNull(Sprinkle sprinkle) {
        return sprinkleTargetRepository.findAllBySprinkleAndReceiverIsNull(sprinkle);
    }
}
