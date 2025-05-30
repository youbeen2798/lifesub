package com.unicorn.lifesub.recommend.service;

import com.unicorn.lifesub.recommend.domain.SpendingCategory;
import com.unicorn.lifesub.recommend.repository.entity.SpendingEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpendingAnalyzer {
    public SpendingCategory analyzeSpending(List<SpendingEntity> spendings) {
        Map<String, Long> totalsByCategory = calculateTotalByCategory(spendings);
        return findTopCategory(totalsByCategory);
    }

    private Map<String, Long> calculateTotalByCategory(List<SpendingEntity> spendings) {
        return spendings.stream()
                .collect(Collectors.groupingBy(
                        SpendingEntity::getCategory,
                        Collectors.summingLong(SpendingEntity::getAmount)
                ));
    }

    private SpendingCategory findTopCategory(Map<String, Long> totals) {
        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> SpendingCategory.builder()
                        .category(entry.getKey())
                        .totalAmount(entry.getValue())
                        .build())
                .orElse(null);
    }
}
