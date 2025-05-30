package com.unicorn.lifesub.member.test.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.lifesub.common.dto.ApiResponse;
import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import com.unicorn.lifesub.member.test.e2e.config.TestContainerConfig;
import com.unicorn.lifesub.member.test.e2e.support.TestDataManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e-test")
@Transactional
class MemberE2ETest extends TestContainerConfig {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private MemberRepository memberRepository;

    private WebTestClient webClient;

    @Value("${test.user.id}")
    private String TEST_USER_ID;

    @Value("${test.user.password}")
    private String TEST_PASSWORD;

    @BeforeEach
    void setUp() {
        webClient = MockMvcWebTestClient
                .bindToApplicationContext(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .configureClient()
                .build();

        memberRepository.deleteAll();
        testDataManager.setupTestData();
    }

    @Test
    @DisplayName("로그인 성공 시나리오")
    void givenValidCredentials_whenLogin_thenSuccess() {
        // Given
        LoginRequest loginRequest = createLoginRequest(TEST_USER_ID, TEST_PASSWORD);

        // When & Then
        webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class)
                .value(response -> {
                    assertThat(response.getStatus()).isEqualTo(200);

                    JwtTokenDTO jwtToken = objectMapper.convertValue(response.getData(), JwtTokenDTO.class);
                    assertThat(jwtToken.getAccessToken()).isNotNull();
                    assertThat(jwtToken.getRefreshToken()).isNotNull();
                });
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패 시나리오")
    void givenInvalidPassword_whenLogin_thenFail() {
        // Given
        LoginRequest loginRequest = createLoginRequest(TEST_USER_ID, "wrongpassword");

        // When & Then
        webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ApiResponse.class)
                .value(response -> {
                    assertThat(response.getStatus()).isEqualTo(210);
                    assertThat(response.getMessage()).isEqualTo("Invalid credentials");
                });
    }

    @Test
    @DisplayName("로그인 후 로그아웃 시나리오")
    void givenValidToken_whenLogout_thenSuccess() {
        // Given - 먼저 로그인
        LoginRequest loginRequest = createLoginRequest(TEST_USER_ID, TEST_PASSWORD);

        ApiResponse loginResponse = webClient.post().uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class)
                .returnResult()
                .getResponseBody();

        JwtTokenDTO tokenDTO = objectMapper.convertValue(loginResponse.getData(), JwtTokenDTO.class);

        // When - 로그아웃
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUserId(TEST_USER_ID);

        // Then
        webClient.post().uri("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDTO.getAccessToken())
                .bodyValue(logoutRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApiResponse.class)
                .value(response -> {
                    assertThat(response.getStatus()).isEqualTo(200);
                    assertThat(response.getMessage()).contains("Success");
                });
    }

    private LoginRequest createLoginRequest(String userId, String password) {
        LoginRequest request = new LoginRequest();
        request.setUserId(userId);
        request.setPassword(password);
        return request;
    }
}