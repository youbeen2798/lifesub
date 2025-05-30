// File: lifesub/recommend/src/main/java/com/unicorn/lifesub/recommend/service/RecommendServiceImpl.java
package com.unicorn.lifesub.recommend.service;

import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.ErrorCode;
import com.unicorn.lifesub.recommend.domain.RecommendedCategory;
import com.unicorn.lifesub.recommend.domain.SpendingCategory;
import com.unicorn.lifesub.recommend.dto.RecommendCategoryDTO;
import com.unicorn.lifesub.recommend.repository.jpa.RecommendRepository;
import com.unicorn.lifesub.recommend.repository.jpa.SpendingRepository;
import com.unicorn.lifesub.recommend.repository.entity.SpendingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final SpendingRepository spendingRepository;
    private final SpendingAnalyzer spendingAnalyzer;

    @Override
    @Transactional(readOnly = true)
    public RecommendCategoryDTO getRecommendedCategory(String userId) {
        LocalDate startDate = LocalDate.now().minusMonths(1);

        List<SpendingEntity> spendings = spendingRepository.findSpendingsByUserIdAndDateAfter(userId, startDate);

        if (spendings.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_SPENDING_DATA);
        }

        SpendingCategory topSpending = spendingAnalyzer.analyzeSpending(spendings);

        if (topSpending == null) {
            throw new BusinessException(ErrorCode.NO_SPENDING_DATA);
        }

        RecommendedCategory recommendedCategory = recommendRepository
                .findBySpendingCategory(topSpending.getCategory())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_RECOMMENDATION_DATA))
                .toDomain();

        return RecommendCategoryDTO.builder()
                .categoryName(recommendedCategory.getRecommendCategory())
                .baseDate(recommendedCategory.getBaseDate())
                .spendingCategory(topSpending.getCategory())
                .totalSpending(topSpending.getTotalAmount())
                .build();
    }

}