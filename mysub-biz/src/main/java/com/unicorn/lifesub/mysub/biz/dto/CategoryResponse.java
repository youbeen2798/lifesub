// CategoryResponse.java
package com.unicorn.lifesub.mysub.biz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private final String categoryId;
    private final String categoryName;
}