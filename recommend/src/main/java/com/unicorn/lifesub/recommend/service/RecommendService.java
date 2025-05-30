package com.unicorn.lifesub.recommend.service;

import com.unicorn.lifesub.recommend.dto.RecommendCategoryDTO;

public interface RecommendService {
    RecommendCategoryDTO getRecommendedCategory(String userId);
}
