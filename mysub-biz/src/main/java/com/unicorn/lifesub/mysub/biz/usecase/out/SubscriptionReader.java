package com.unicorn.lifesub.mysub.biz.usecase.out;

import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionReader {
    Optional<Subscription> findById(Long id);
    List<Subscription> findByCategory(String categoryId);
    List<Category> findAllCategories();
}
