package com.unicorn.lifesub.mysub.infra.controller;

import com.unicorn.lifesub.common.dto.ApiResponse;
import com.unicorn.lifesub.mysub.biz.dto.CategoryResponse;
import com.unicorn.lifesub.mysub.biz.dto.ServiceListResponse;
import com.unicorn.lifesub.mysub.biz.usecase.in.CategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "구독 카테고리 API", description = "구독 카테고리 관련 API")
@RestController
@SecurityRequirement(name = "bearerAuth")    //이 어노테이션이 없으면 요청 헤더에 Authorization헤더가 안 생김
@RequestMapping("/api/mysub")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryUseCase categoryUseCase;

    @Operation(summary = "전체 카테고리 목록 조회")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryUseCase.getAllCategories()));
    }

    @Operation(summary = "카테고리별 서비스 목록 조회")
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceListResponse>>> getServicesByCategory(
            @RequestParam String categoryId) {
        return ResponseEntity.ok(ApiResponse.success(categoryUseCase.getServicesByCategory(categoryId)));
    }
}
