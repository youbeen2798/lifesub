package com.unicorn.lifesub.member.test.integration.service;

import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.member.config.jwt.JwtTokenProvider;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import com.unicorn.lifesub.member.service.MemberServiceImpl;
import com.unicorn.lifesub.member.test.integration.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.unicorn.lifesub.member.test.integration.support.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("integration-test")
class MemberServiceIntegrationTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("유효한 자격증명으로 로그인 성공")
    void givenValidCredentials_whenLogin_thenReturnJwtToken() {
        // Given
        LoginRequest loginRequest = createLoginRequest();
        MemberEntity memberEntity = createMemberEntity();

        given(memberRepository.findByUserId(TEST_USER_ID)).willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches(TEST_PASSWORD, memberEntity.getPassword())).willReturn(true);
        given(jwtTokenProvider.createToken(eq(memberEntity), any())).willReturn(
                new JwtTokenDTO("accessToken", "refreshToken"));

        // When
        JwtTokenDTO result = memberService.login(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");

        verify(memberRepository).findByUserId(TEST_USER_ID);
        verify(passwordEncoder).matches(TEST_PASSWORD, memberEntity.getPassword());
        verify(jwtTokenProvider).createToken(eq(memberEntity), any());
    }

    @Test
    @DisplayName("로그아웃 처리 성공")
    void givenUserId_whenLogout_thenSuccess() {
        // Given
        LogoutRequest request = new LogoutRequest();
        request.setUserId(TEST_USER_ID);

        // When
        LogoutResponse response = memberService.logout(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).contains("로그아웃");
    }
}