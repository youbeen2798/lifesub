package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MySubscriptionJpaRepository extends JpaRepository<MySubscriptionEntity, Long> {
    List<MySubscriptionEntity> findByUserId(String userId);
    Optional<MySubscriptionEntity> findBySubscription_Id(Long subscriptionId);
}
