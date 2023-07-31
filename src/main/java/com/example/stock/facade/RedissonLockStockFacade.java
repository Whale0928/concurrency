package com.example.stock.facade;

import com.example.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockStockFacade {
    private RedissonClient redissonClient;
    private StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {

        RLock lock = redissonClient.getLock(id.toString());
        try {
            //lock.tryLock(10, 1, TimeUnit.SECONDS : 15초 동안 기다리고 1초마다 retry
            //available : 유효한  , 사용가능한
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);

            if (!available) {
                log.info("lock is not available : 락을 점유 실패.");
                return;
            }

            stockService.decrease(id, quantity);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
