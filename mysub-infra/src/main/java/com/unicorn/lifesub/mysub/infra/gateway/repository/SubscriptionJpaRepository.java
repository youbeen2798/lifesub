package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// SubscriptionJpaRepository.java에 메서드 추가
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByCategory(String category);
}