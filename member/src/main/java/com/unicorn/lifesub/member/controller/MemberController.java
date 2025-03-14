package com.unicorn.lifesub.member.controller;

import com.unicorn.lifesub.common.dto.ApiResponse;
import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.InfraException;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 인증 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "로그인", description = "사용자 ID와 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenDTO>> login(@Valid @RequestBody LoginRequest request) {
        try {
            JwtTokenDTO response = memberService.login(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getErrorCode()));
        } catch (InfraException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getErrorCode()));
        }
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@Valid @RequestBody LogoutRequest request) {
        LogoutResponse response = memberService.logout(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
