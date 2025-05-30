package com.unicorn.lifesub.mysub.biz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MySubResponse {
    private final Long id;
    private final String serviceName;
    private final String logoUrl;
}
