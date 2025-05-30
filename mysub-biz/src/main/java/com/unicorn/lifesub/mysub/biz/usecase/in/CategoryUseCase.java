package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.biz.dto.CategoryResponse;
import com.unicorn.lifesub.mysub.biz.dto.ServiceListResponse;
import java.util.List;

public interface CategoryUseCase {
    List<CategoryResponse> getAllCategories();
    List<ServiceListResponse> getServicesByCategory(String categoryId);
}
