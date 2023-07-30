package com.example.stock.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long stock;

    public Stock(Long productId, Long stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public void decrease(Long quantity) {
        if ((this.stock - quantity) < 0) throw new IllegalStateException("재고가 부족합니다.");
        this.stock -= quantity;
    }
}
