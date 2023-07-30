package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {
    private final OptimisticLockStockService optimisticLockStockService;

    /**
     * 버전 관리를 통한 재고 감소
     *
     * @param id       the id
     * @param quantity the quantity
     * @throws InterruptedException the interrupted exception
     */
    public void decreed(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decreed(id, quantity);
                break;
            } catch (Exception e) {
                sleep(50);//정상적으로 처리 되지 않았을떄 50ms 대기 후 재시도
            }
        }

    }

}
