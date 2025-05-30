package com.unicorn.lifesub.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class RecommendCategoryDTO {
    private String categoryName;
    private LocalDate baseDate;
    private String spendingCategory;
    private Long totalSpending;
}
