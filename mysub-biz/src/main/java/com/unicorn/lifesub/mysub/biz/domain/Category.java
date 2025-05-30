// Category.java
package com.unicorn.lifesub.mysub.biz.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Category {
    private final String categoryId;
    private final String name;

    @Builder
    public Category(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}