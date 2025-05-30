package com.unicorn.lifesub.mysub.biz.usecase.out;

import com.unicorn.lifesub.mysub.biz.domain.MySubscription;

public interface MySubscriptionWriter {
    MySubscription save(String userId, Long subscriptionId);
    void delete(Long id);
}
