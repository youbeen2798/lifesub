package com.unicorn.lifesub.mysub.biz.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MySubscription {
    private final Long id;
    private final String userId;
    private final Subscription subscription;

    @Builder
    public MySubscription(Long id, String userId, Subscription subscription) {
        this.id = id;
        this.userId = userId;
        this.subscription = subscription;
    }

    public int getPrice() {
        return subscription.getPrice();
    }
}
