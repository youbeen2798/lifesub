package com.unicorn.lifesub.recommend.domain;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class RecommendedCategory {
    private final String spendingCategory;
    private final String recommendCategory;
    private final LocalDate baseDate;

    @Builder
    public RecommendedCategory(String spendingCategory, String recommendCategory, LocalDate baseDate) {
        this.spendingCategory = spendingCategory;
        this.recommendCategory = recommendCategory;
        this.baseDate = baseDate;
    }
}
