package com.unicorn.lifesub.recommend.repository.jpa;

import com.unicorn.lifesub.recommend.repository.entity.RecommendedCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<RecommendedCategoryEntity, Long> {
    Optional<RecommendedCategoryEntity> findBySpendingCategory(String category);
}