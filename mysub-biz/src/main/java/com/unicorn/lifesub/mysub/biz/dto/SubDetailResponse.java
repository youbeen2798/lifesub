package com.unicorn.lifesub.mysub.biz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class SubDetailResponse {
    private final String serviceName;
    private final String logoUrl;
    private final String category;
    private final String description;
    private final int price;
    private final int maxSharedUsers;
}
