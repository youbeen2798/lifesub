package com.unicorn.lifesub.mysub.biz.usecase.out;

import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import java.util.List;
import java.util.Optional;

public interface MySubscriptionReader {
    List<MySubscription> findByUserId(String userId);
    Optional<MySubscription> findById(Long id);
}
