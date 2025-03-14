package com.unicorn.lifesub.member.test.unit.service;

import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.ErrorCode;
import com.unicorn.lifesub.common.exception.InfraException;
import com.unicorn.lifesub.member.config.jwt.JwtTokenProvider;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import com.unicorn.lifesub.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

/**
 * 멤버 서비스 테스트 클래스
 * 주요 비즈니스 로직인 로그인/로그아웃 기능을 검증
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplUnitTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    // 테스트용 상수 정의
    private static final String TEST_USER_ID = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_USER_NAME = "Test User";

    /**
     * 로그인 성공 케이스 테스트
     * 올바른 사용자 ID와 비밀번호로 로그인 시 JWT 토큰이 정상적으로 발급되는지 검증
     */
    @Test
    @DisplayName("givenValidCredentials_whenLogin_thenSuccess")
    void givenValidCredentials_whenLogin_thenSuccess() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUserId(TEST_USER_ID);
        request.setPassword(TEST_PASSWORD);

        MemberEntity memberEntity = createTestMemberEntity();
        JwtTokenDTO expectedToken = createTestJwtTokenDTO();

        given(memberRepository.findByUserId(TEST_USER_ID)).willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches(TEST_PASSWORD, memberEntity.getPassword())).willReturn(true);
        given(jwtTokenProvider.createToken(any(), any())).willReturn(expectedToken);

        // When
        JwtTokenDTO result = memberService.login(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(expectedToken.getAccessToken());
        assertThat(result.getRefreshToken()).isEqualTo(expectedToken.getRefreshToken());
    }

    /**
     * 로그인 실패 케이스 테스트 - 사용자가 존재하지 않는 경우
     * 존재하지 않는 사용자 ID로 로그인 시도 시 적절한 예외가 발생하는지 검증
     */
    @Test
    @DisplayName("givenNonExistentUser_whenLogin_thenThrowException")
    void givenNonExistentUser_whenLogin_thenThrowException() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUserId("nonexistent");
        request.setPassword(TEST_PASSWORD);

        when(memberRepository.findByUserId("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        InfraException exception = assertThrows(InfraException.class,
                () -> memberService.login(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    /**
     * 로그인 실패 케이스 테스트 - 잘못된 비밀번호
     * 올바른 사용자 ID와 잘못된 비밀번호로 로그인 시도 시 적절한 예외가 발생하는지 검증
     */
    @Test
    @DisplayName("givenInvalidPassword_whenLogin_thenThrowException")
    void givenInvalidPassword_whenLogin_thenThrowException() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUserId(TEST_USER_ID);
        request.setPassword("wrongPassword");

        MemberEntity memberEntity = createTestMemberEntity();

        given(memberRepository.findByUserId(TEST_USER_ID)).willReturn(Optional.of(memberEntity));
        given(passwordEncoder.matches("wrongPassword", memberEntity.getPassword())).willReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> memberService.login(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    }

    /**
     * 로그아웃 테스트
     * 로그아웃 요청 시 정상적으로 처리되는지 검증
     */
    @Test
    @DisplayName("givenLogoutRequest_whenLogout_thenSuccess")
    void givenLogoutRequest_whenLogout_thenSuccess() {
        // Given
        LogoutRequest request = new LogoutRequest();
        request.setUserId(TEST_USER_ID);

        // When
        var response = memberService.logout(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).contains("로그아웃이 완료되었습니다");
    }

    // 테스트 헬퍼 메서드
    private MemberEntity createTestMemberEntity() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        return MemberEntity.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();
    }

    private JwtTokenDTO createTestJwtTokenDTO() {
        return JwtTokenDTO.builder()
                .accessToken("test-access-token")
                .refreshToken("test-refresh-token")
                .build();
    }
}
