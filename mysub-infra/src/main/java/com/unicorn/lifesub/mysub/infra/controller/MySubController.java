package com.unicorn.lifesub.mysub.infra.controller;

import com.unicorn.lifesub.common.dto.ApiResponse;
import com.unicorn.lifesub.mysub.biz.dto.MySubResponse;
import com.unicorn.lifesub.mysub.biz.dto.TotalFeeResponse;
import com.unicorn.lifesub.mysub.biz.usecase.in.MySubscriptionsUseCase;
import com.unicorn.lifesub.mysub.biz.usecase.in.TotalFeeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "마이구독 API", description = "마이구독 관련 API")
@RestController
@SecurityRequirement(name = "bearerAuth")    //이 어노테이션이 없으면 요청 헤더에 Authorization헤더가 안 생김
@RequestMapping("/api/mysub")
@RequiredArgsConstructor
public class MySubController {
    private final TotalFeeUseCase totalFeeUseCase;
    private final MySubscriptionsUseCase mySubscriptionsUseCase;

    @Operation(summary = "총 구독료 조회", description = "사용자의 총 구독료를 조회합니다.")
    @GetMapping("/total-fee")
    public ResponseEntity<ApiResponse<TotalFeeResponse>> getTotalFee(@RequestParam String userId) {
        TotalFeeResponse response = totalFeeUseCase.getTotalFee(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "구독 목록 조회", description = "사용자의 구독 서비스 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MySubResponse>>> getMySubscriptions(@RequestParam String userId) {
        List<MySubResponse> response = mySubscriptionsUseCase.getMySubscriptions(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
