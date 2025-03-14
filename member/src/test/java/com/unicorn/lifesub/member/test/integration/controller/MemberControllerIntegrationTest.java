package com.unicorn.lifesub.member.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.member.controller.MemberController;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.service.MemberService;
import com.unicorn.lifesub.member.test.integration.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MemberController 통합 테스트 클래스
 * 회원 인증 관련 API의 요청/응답을 검증합니다.
 *
 * @author Tera
 * @version 1.0
 */
@WebMvcTest(MemberController.class)
@Import({MemberControllerIntegrationTest.TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("integration-test")
class MemberControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    /**
     * 테스트를 위한 설정 클래스입니다.
     * Controller와 의존성들을 직접 등록하여 테스트 환경을 구성합니다.
     *
     * @implNote 1. Controller와 필요한 빈들을 직접 등록하여 의존성을 명확히 합니다.
     *          2. Service는 Mockito를 통해 mock 객체로 대체하여 테스트를 격리합니다.
     */
    @Configuration
    static class TestConfig implements WebMvcConfigurer {

        /**
         * MemberController 빈을 등록합니다.
         * memberService()를 통해 Mock 객체를 주입받아 Controller를 생성합니다.
         *
         * @return MemberController 인스턴스
         */
        @Bean
        public MemberController memberController() {
            return new MemberController(memberService());
        }

        /**
         * MemberService Mock 객체를 생성하여 반환합니다.
         * 실제 Service 구현체 대신 Mock 객체를 사용하여 테스트를 격리합니다.
         *
         * @return MemberService Mock 객체
         */
        @Bean
        public MemberService memberService() {
            return mock(MemberService.class);
        }
    }

    /**
     * 로그인 성공 케이스를 테스트합니다.
     * 올바른 사용자 ID와 비밀번호로 요청 시 JWT 토큰이 정상적으로 발급되는지 검증합니다.
     *
     * @throws Exception MockMvc 수행 중 발생할 수 있는 예외
     */
    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {
        // Given: 테스트에 필요한 요청 데이터와 응답 객체를 준비
        LoginRequest request = new LoginRequest();
        request.setUserId("testUser");
        request.setPassword("testPass");

        JwtTokenDTO tokenDTO = JwtTokenDTO.builder()
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .build();

        given(memberService.login(any(LoginRequest.class))).willReturn(tokenDTO);

        // When & Then: API 호출 결과 검증
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("test-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("test-refresh-token"));
    }

    /**
     * 로그인 실패 케이스를 테스트합니다.
     * 잘못된 형식의 요청 데이터로 API 호출 시 적절한 에러 응답이 반환되는지 검증합니다.
     *
     * @throws Exception MockMvc 수행 중 발생할 수 있는 예외
     */
    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 요청 형식")
    void loginFailInvalidRequest() throws Exception {
        // Given: 검증에 실패하도록 필수 필드가 비어있는 요청 객체 생성
        LoginRequest request = new LoginRequest();

        // When & Then: API 호출 결과 검증
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * 로그아웃 성공 케이스를 테스트합니다.
     * 정상적인 로그아웃 요청 시 성공 응답이 반환되는지 검증합니다.
     *
     * @throws Exception MockMvc 수행 중 발생할 수 있는 예외
     */
    @Test
    @DisplayName("로그아웃 성공 테스트")
    void logoutSuccess() throws Exception {
        // Given: 테스트에 필요한 요청 데이터와 응답 객체를 준비
        LogoutRequest request = new LogoutRequest();
        request.setUserId("testUser");

        LogoutResponse response = LogoutResponse.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();

        given(memberService.logout(any(LogoutRequest.class))).willReturn(response);

        // When & Then: API 호출 결과 검증
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.message").value("로그아웃이 완료되었습니다."));
    }

    /**
     * 로그아웃 실패 케이스를 테스트합니다.
     * 잘못된 형식의 요청 데이터로 API 호출 시 적절한 에러 응답이 반환되는지 검증합니다.
     *
     * @throws Exception MockMvc 수행 중 발생할 수 있는 예외
     */
    @Test
    @DisplayName("로그아웃 실패 테스트 - 잘못된 요청 형식")
    void logoutFailInvalidRequest() throws Exception {
        // Given: 검증에 실패하도록 필수 필드가 비어있는 요청 객체 생성
        LogoutRequest request = new LogoutRequest();

        // When & Then: API 호출 결과 검증
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}