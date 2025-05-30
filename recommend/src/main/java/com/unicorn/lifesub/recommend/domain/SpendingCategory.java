package com.unicorn.lifesub.recommend.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpendingCategory {
    private final String category;
    private final Long totalAmount;

    @Builder
    public SpendingCategory(String category, Long totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }
}
