package com.unicorn.lifesub.recommend.repository.entity;

import com.unicorn.lifesub.recommend.domain.RecommendedCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommended_categories")
@Getter
@NoArgsConstructor
public class RecommendedCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String spendingCategory;
    private String recommendCategory;

    @Builder
    public RecommendedCategoryEntity(Long id, String spendingCategory, String recommendCategory) {
        this.id = id;
        this.spendingCategory = spendingCategory;
        this.recommendCategory = recommendCategory;
    }

    public RecommendedCategory toDomain() {
        return RecommendedCategory.builder()
                .spendingCategory(spendingCategory)
                .recommendCategory(recommendCategory)
                .baseDate(java.time.LocalDate.now())
                .build();
    }
}
