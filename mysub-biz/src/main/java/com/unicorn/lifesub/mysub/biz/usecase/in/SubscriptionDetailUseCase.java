package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.biz.dto.SubDetailResponse;

public interface SubscriptionDetailUseCase {
    SubDetailResponse getSubscriptionDetail(Long subscriptionId);
}
