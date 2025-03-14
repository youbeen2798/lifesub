package com.unicorn.lifesub.member.test.unit.domain;

import com.unicorn.lifesub.member.domain.Member;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Member 도메인 객체 테스트 클래스
 * 도메인 객체의 생성 및 엔티티 변환 로직을 검증
 */
class MemberUnitTest {

    // 테스트용 상수 정의
    private static final String TEST_USER_ID = "testUser";
    private static final String TEST_USER_NAME = "Test User";
    private static final String TEST_PASSWORD = "testPassword";

    /**
     * Member 객체 생성 테스트
     * Builder 패턴을 사용한 Member 객체 생성이 정상적으로 동작하는지 검증
     */
    @Test
    @DisplayName("givenMemberInfo_whenBuildMember_thenSuccess")
    void givenMemberInfo_whenBuildMember_thenSuccess() {
        // Given
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        // When
        Member member = Member.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();

        // Then
        assertThat(member).isNotNull();
        assertThat(member.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(member.getUserName()).isEqualTo(TEST_USER_NAME);
        assertThat(member.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(member.getRoles()).containsExactly("USER");
    }

    /**
     * Entity에서 Domain 객체로의 변환 테스트
     * MemberEntity.toDomain() 메서드가 정상적으로 동작하는지 검증
     */
    @Test
    @DisplayName("givenMemberEntity_whenConvertToDomain_thenSuccess")
    void givenMemberEntity_whenConvertToDomain_thenSuccess() {
        // Given
        MemberEntity entity = createTestMemberEntity();

        // When
        Member member = entity.toDomain();

        // Then
        assertThat(member).isNotNull();
        assertThat(member.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(member.getUserName()).isEqualTo(TEST_USER_NAME);
        assertThat(member.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(member.getRoles()).containsExactly("USER");
    }

    /**
     * Domain 객체에서 Entity로의 변환 테스트
     * MemberEntity.fromDomain() 메서드가 정상적으로 동작하는지 검증
     */
    @Test
    @DisplayName("givenMemberDomain_whenConvertToEntity_thenSuccess")
    void givenMemberDomain_whenConvertToEntity_thenSuccess() {
        // Given
        Member member = createTestMember();

        // When
        MemberEntity entity = MemberEntity.fromDomain(member);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(entity.getUserName()).isEqualTo(TEST_USER_NAME);
        assertThat(entity.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(entity.getRoles()).containsExactly("USER");
    }

    /**
     * 다중 역할을 가진 Member 객체 생성 테스트
     * 여러 역할을 가진 Member 객체가 정상적으로 생성되는지 검증
     */
    @Test
    @DisplayName("givenMultipleRoles_whenBuildMember_thenSuccess")
    void givenMultipleRoles_whenBuildMember_thenSuccess() {
        // Given
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");

        // When
        Member member = Member.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();

        // Then
        assertThat(member).isNotNull();
        assertThat(member.getRoles()).hasSize(2);
        assertThat(member.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN");
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

    private Member createTestMember() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        return Member.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();
    }
}
