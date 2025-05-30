package com.unicorn.lifesub.member.test.integration.repository;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MemberRepository 통합 테스트
 * TestContainers를 사용하여 실제 PostgreSQL DB와의 상호작용을 테스트합니다.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("integration-test")
@Transactional
class MemberRepositoryIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2-alpine")
            .withDatabaseName("member")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("회원 저장 및 조회 테스트")
    void givenMemberEntity_whenSave_thenFindByUserId() {
        // Given
        String userId = "testUser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        MemberEntity member = MemberEntity.builder()
                .userId(userId)
                .userName("Test User")
                .password("encodedPassword")
                .roles(roles)
                .build();

        // When
        entityManager.persistAndFlush(member);
        entityManager.clear(); // 1차 캐시 클리어

        // Then
        Optional<MemberEntity> foundMember = memberRepository.findByUserId(userId);
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getUserId()).isEqualTo(userId);
        assertThat(foundMember.get().getUserName()).isEqualTo("Test User");
        assertThat(foundMember.get().getRoles()).containsExactly("USER");
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 테스트")
    void givenNonExistentUserId_whenFindByUserId_thenReturnEmpty() {
        // Given
        String nonExistentUserId = "nonexistent";

        // When
        Optional<MemberEntity> result = memberRepository.findByUserId(nonExistentUserId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("회원 정보 업데이트 테스트")
    void givenExistingMember_whenUpdateInfo_thenSuccess() {
        // Given
        String userId = "testUser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        MemberEntity member = MemberEntity.builder()
                .userId(userId)
                .userName("Original Name")
                .password("originalPassword")
                .roles(roles)
                .build();

        entityManager.persistAndFlush(member);
        entityManager.clear();

        // When
        MemberEntity foundMember = memberRepository.findByUserId(userId).get();
        foundMember.updateUserName("Updated Name");
        entityManager.persistAndFlush(foundMember);
        entityManager.clear();

        // Then
        MemberEntity updatedMember = memberRepository.findByUserId(userId).get();
        assertThat(updatedMember.getUserName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void givenExistingMember_whenDelete_thenCannotFind() {
        // Given
        String userId = "testUser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");

        MemberEntity member = MemberEntity.builder()
                .userId(userId)
                .userName("Test User")
                .password("password")
                .roles(roles)
                .build();

        entityManager.persistAndFlush(member);
        entityManager.clear();

        // When
        MemberEntity foundMember = memberRepository.findByUserId(userId).get();
        memberRepository.delete(foundMember);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<MemberEntity> deletedMember = memberRepository.findByUserId(userId);
        assertThat(deletedMember).isEmpty();
    }
}