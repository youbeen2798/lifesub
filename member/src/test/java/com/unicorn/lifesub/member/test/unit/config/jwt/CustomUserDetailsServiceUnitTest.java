package com.unicorn.lifesub.member.test.unit.config.jwt;

import com.unicorn.lifesub.member.config.jwt.CustomUserDetailsService;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

/**
 * 사용자 상세 정보 서비스 테스트 클래스
 * Spring Security의 UserDetailsService 구현체 검증
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceUnitTest {

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Mock
    private MemberRepository memberRepository;

    // 테스트용 상수 정의
    private static final String TEST_USER_ID = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_USER_NAME = "Test User";

    /**
     * 사용자 조회 성공 케이스 테스트
     * 존재하는 사용자 ID로 조회 시 UserDetails 객체가 정상적으로 반환되는지 검증
     */
    @Test
    @DisplayName("givenExistingUserId_whenLoadUser_thenReturnUserDetails")
    void givenExistingUserId_whenLoadUser_thenReturnUserDetails() {
        // Given
        MemberEntity memberEntity = createTestMemberEntity();
        given(memberRepository.findByUserId(TEST_USER_ID)).willReturn(Optional.of(memberEntity));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_USER_ID);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(TEST_USER_ID);
        assertThat(userDetails.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(userDetails.getAuthorities()).hasSize(1);
    }

    /**
     * 사용자 조회 실패 케이스 테스트
     * 존재하지 않는 사용자 ID로 조회 시 적절한 예외가 발생하는지 검증
     */
    @Test
    @DisplayName("givenNonExistentUserId_whenLoadUser_thenThrowException")
    void givenNonExistentUserId_whenLoadUser_thenThrowException() {
        // Given
        String nonExistentUserId = "nonexistent";
        given(memberRepository.findByUserId(nonExistentUserId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername(nonExistentUserId));
    }

    /**
     * 사용자 권한 매핑 테스트
     * 사용자의 역할이 Spring Security 권한으로 올바르게 매핑되는지 검증
     */
    @Test
    @DisplayName("givenUserWithRoles_whenLoadUser_thenMapAuthoritiesCorrectly")
    void givenUserWithRoles_whenLoadUser_thenMapAuthoritiesCorrectly() {
        // Given
        MemberEntity memberEntity = createTestMemberEntityWithMultipleRoles();
        given(memberRepository.findByUserId(TEST_USER_ID)).willReturn(Optional.of(memberEntity));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_USER_ID);

        // Then
        assertThat(userDetails.getAuthorities()).hasSize(2);
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("USER", "ADMIN");
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

    private MemberEntity createTestMemberEntityWithMultipleRoles() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");
        return MemberEntity.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();
    }
}