package com.unicorn.lifesub.mysub.biz.service;

import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.dto.*;
import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.biz.usecase.out.*;
import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MySubscriptionService implements
        TotalFeeUseCase,
        MySubscriptionsUseCase,
        SubscriptionDetailUseCase,
        SubscribeUseCase,
        CancelSubscriptionUseCase,
        CategoryUseCase {

    private final MySubscriptionReader mySubscriptionReader;
    private final MySubscriptionWriter mySubscriptionWriter;
    private final SubscriptionReader subscriptionReader;

    @Override
    @Transactional(readOnly = true)
    public TotalFeeResponse getTotalFee(String userId) {
        List<MySubscription> subscriptions = mySubscriptionReader.findByUserId(userId);
        long totalFee = subscriptions.stream()
                .mapToLong(MySubscription::getPrice)
                .sum();

        return TotalFeeResponse.builder()
                .totalFee(totalFee)
                .feeLevel(calculateFeeLevel(totalFee))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MySubResponse> getMySubscriptions(String userId) {
        return mySubscriptionReader.findByUserId(userId).stream()
                .map(subscription -> MySubResponse.builder()
                        .id(subscription.getSubscription().getId())
                        .serviceName(subscription.getSubscription().getName())
                        .logoUrl(subscription.getSubscription().getLogoUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubDetailResponse getSubscriptionDetail(Long subscriptionId) {
        Subscription subscription = subscriptionReader.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return SubDetailResponse.builder()
                .serviceName(subscription.getName())
                .logoUrl(subscription.getLogoUrl())
                .category(subscription.getCategory())
                .description(subscription.getDescription())
                .price(subscription.getPrice())
                .maxSharedUsers(subscription.getMaxSharedUsers())
                .build();
    }

    @Override
    @Transactional
    public void subscribe(Long subscriptionId, String userId) {
        // 구독 서비스 존재 확인
        subscriptionReader.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        mySubscriptionWriter.save(userId, subscriptionId);
    }

    @Override
    @Transactional
    public void cancel(Long subscriptionId) {
        mySubscriptionWriter.delete(subscriptionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return subscriptionReader.findAllCategories().stream()
                .map(category -> CategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceListResponse> getServicesByCategory(String categoryId) {
        return subscriptionReader.findByCategory(categoryId).stream()
                .map(subscription -> ServiceListResponse.builder()
                        .serviceId(subscription.getId().toString())
                        .serviceName(subscription.getName())
                        .description(subscription.getDescription())
                        .price(subscription.getPrice())
                        .logoUrl(subscription.getLogoUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Value("${fee.level.collector}")
    private long collectorThreshold;
    @Value("${fee.level.addict}")
    private long addictThreshold;
    private String calculateFeeLevel(long totalFee) {
        if (totalFee < collectorThreshold) return FeeLevel.LIKFER.getFeeLevel();
        if (totalFee < addictThreshold) return FeeLevel.COLLECTOR.getFeeLevel();
        return FeeLevel.ADDICT.getFeeLevel();
    }
}