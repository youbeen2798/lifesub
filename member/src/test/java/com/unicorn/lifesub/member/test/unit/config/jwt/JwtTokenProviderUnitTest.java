package com.unicorn.lifesub.member.test.unit.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.common.exception.InfraException;
import com.unicorn.lifesub.member.config.jwt.JwtTokenProvider;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JWT 토큰 제공자 테스트 클래스
 * 토큰 생성, 검증, 파싱 등의 기능을 검증
 */
class JwtTokenProviderUnitTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String SECRET_KEY = "test-secret-key";
    private static final long ACCESS_TOKEN_VALIDITY = 3600000;
    private static final long REFRESH_TOKEN_VALIDITY = 86400000;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY);
    }

    /**
     * 토큰 생성 테스트
     * 유효한 사용자 정보로 JWT 토큰이 정상적으로 생성되는지 검증
     */
    @Test
    @DisplayName("givenValidMember_whenCreateToken_thenSuccess")
    void givenValidMember_whenCreateToken_thenSuccess() {
        // Given
        MemberEntity member = createTestMemberEntity();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));

        // When
        JwtTokenDTO tokens = jwtTokenProvider.createToken(member, authorities);

        // Then
        assertThat(tokens).isNotNull();
        assertThat(tokens.getAccessToken()).isNotNull();
        assertThat(tokens.getRefreshToken()).isNotNull();
        assertThat(jwtTokenProvider.validateToken(tokens.getAccessToken())).isEqualTo(1);
    }

    /**
     * 토큰 검증 테스트
     * 유효한 토큰과 유효하지 않은 토큰에 대한 검증이 정상적으로 동작하는지 확인
     */
    @Test
    @DisplayName("givenToken_whenValidate_thenSuccess")
    void givenToken_whenValidate_thenSuccess() {
        // Given
        String token = createValidToken();

        // When & Then
        assertThat(jwtTokenProvider.validateToken(token)).isEqualTo(1);
    }

    /**
     * 인증 정보 추출 테스트
     * JWT 토큰에서 인증 정보가 정상적으로 추출되는지 검증
     */
    @Test
    @DisplayName("givenValidToken_whenGetAuthentication_thenSuccess")
    void givenValidToken_whenGetAuthentication_thenSuccess() {
        // Given
        String token = createValidToken();

        // When
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // Then
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getAuthorities()).hasSize(1);
    }

    /**
     * 토큰 추출 테스트
     * HTTP 요청 헤더에서 토큰이 정상적으로 추출되는지 검증
     */
    @Test
    @DisplayName("givenRequest_whenResolveToken_thenSuccess")
    void givenRequest_whenResolveToken_thenSuccess() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "test-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // When
        String resolvedToken = jwtTokenProvider.resolveToken(request);

        // Then
        assertThat(resolvedToken).isEqualTo(token);
    }

    /**
     * 유효하지 않은 토큰 검증 테스트
     * 잘못된 형식의 토큰에 대해 적절한 예외가 발생하는지 검증
     */
    @Test
    @DisplayName("givenInvalidToken_whenValidate_thenThrowException")
    void givenInvalidToken_whenValidate_thenThrowException() {
        // Given
        String invalidToken = "invalid-token";

        // When & Then
        assertThrows(InfraException.class, () -> jwtTokenProvider.validateToken(invalidToken));
    }

    // 테스트 헬퍼 메서드
    private MemberEntity createTestMemberEntity() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        return MemberEntity.builder()
                .userId("testUser")
                .userName("Test User")
                .password("password")
                .roles(roles)
                .build();
    }

    private String createValidToken() {
        Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
        return JWT.create()
                .withSubject("testUser")
                .withClaim("auth", Collections.singletonList("ROLE_USER"))
                .sign(algorithm);
    }
}