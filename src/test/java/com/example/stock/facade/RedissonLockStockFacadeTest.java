package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private RedissonLockStockFacade stockService;

    @Autowired
    private StockRepository stockRepository;
    @BeforeEach
    void setUp() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.save(stock);
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100개의 요청이 올 수 있다.")
    void test2() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(20  );
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit((
                    () -> {
                        try {
                            stockService.decrease(1L, 1L);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
            ));
        }
        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0L, stock.getStock());
    }
}