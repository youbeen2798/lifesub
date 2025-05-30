package com.unicorn.lifesub.mysub.biz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class ServiceListResponse {
    private String serviceId;
    private String serviceName;
    private String description;
    private int price;
    private String logoUrl;
}
