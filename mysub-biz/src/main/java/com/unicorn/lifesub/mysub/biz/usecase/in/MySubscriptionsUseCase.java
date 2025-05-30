package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.biz.dto.MySubResponse;
import java.util.List;

public interface MySubscriptionsUseCase {
    List<MySubResponse> getMySubscriptions(String userId);
}
