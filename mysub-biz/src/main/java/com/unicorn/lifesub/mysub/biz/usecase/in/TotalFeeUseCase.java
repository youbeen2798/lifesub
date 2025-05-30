package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.biz.dto.TotalFeeResponse;

public interface TotalFeeUseCase {
    TotalFeeResponse getTotalFee(String userId);
}
