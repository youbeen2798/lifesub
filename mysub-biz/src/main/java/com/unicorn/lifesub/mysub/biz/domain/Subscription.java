package com.unicorn.lifesub.mysub.biz.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Subscription {
    private final Long id;
    private final String name;
    private final String description;
    private final String category;
    private final int price;
    private final int maxSharedUsers;
    private final String logoUrl;

    @Builder
    public Subscription(Long id, String name, String description, String category, 
                       int price, int maxSharedUsers, String logoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.maxSharedUsers = maxSharedUsers;
        this.logoUrl = logoUrl;
    }
}
