package com.unicorn.lifesub.mysub.infra.gateway;

import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.ErrorCode;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.usecase.out.MySubscriptionReader;
import com.unicorn.lifesub.mysub.biz.usecase.out.MySubscriptionWriter;
import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.MySubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MySubscriptionGateway implements MySubscriptionReader, MySubscriptionWriter {
    private final MySubscriptionJpaRepository mySubscriptionRepository;
    private final SubscriptionJpaRepository subscriptionRepository;

    @Override
    public List<MySubscription> findByUserId(String userId) {
        return mySubscriptionRepository.findByUserId(userId).stream()
                .map(MySubscriptionEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MySubscription> findById(Long id) {
        return mySubscriptionRepository.findById(id)
                .map(MySubscriptionEntity::toDomain);
    }

    @Override
    public MySubscription save(String userId, Long subscriptionId) {
        SubscriptionEntity subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        MySubscriptionEntity entity = MySubscriptionEntity.builder()
                .userId(userId)
                .subscription(subscription)
                .build();

        return mySubscriptionRepository.save(entity).toDomain();
    }

    @Override
    @Transactional
    public void delete(Long subscriptionId) {
        MySubscriptionEntity entity = mySubscriptionRepository.findBySubscription_Id(subscriptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        mySubscriptionRepository.delete(entity);
    }
}
