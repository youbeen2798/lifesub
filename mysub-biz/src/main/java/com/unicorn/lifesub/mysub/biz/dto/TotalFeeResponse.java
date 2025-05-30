package com.unicorn.lifesub.mysub.biz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalFeeResponse {
    private final Long totalFee;
    private final String feeLevel;
}
