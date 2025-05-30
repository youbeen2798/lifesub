package com.unicorn.lifesub.mysub.infra.gateway;

import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.usecase.out.SubscriptionReader;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionGateway implements SubscriptionReader {
    private final SubscriptionJpaRepository subscriptionRepository;
    private final CategoryJpaRepository categoryRepository;

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id)
                .map(SubscriptionEntity::toDomain);
    }

    @Override
    public List<Subscription> findByCategory(String categoryId) {
        return subscriptionRepository.findByCategory(categoryId).stream()
                .map(SubscriptionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryEntity::toDomain)
                .collect(Collectors.toList());
    }
}
