// TestDataManager.java
package com.unicorn.lifesub.member.test.e2e.support;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TestDataManager {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${test.user.id}")
    private String TEST_USER_ID;

    @Value("${test.user.password}")
    private String TEST_PASSWORD;

    @Value("${test.user.name}")
    private String TEST_USER_NAME;

    @Transactional
    public void setupTestData() {
        if (memberRepository.count() == 0) {
            // 기본 테스트 사용자 생성
            createTestUser();
            // 추가 테스트 데이터 생성
            createAdditionalTestData();
        }
    }

    private void createTestUser() {
        Set<String> userRoles = new HashSet<>();
        userRoles.add("USER");

        MemberEntity testUser = MemberEntity.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USER_NAME)
                .password(passwordEncoder.encode(TEST_PASSWORD))  // 암호화된 비밀번호 저장
                .roles(userRoles)
                .build();

        memberRepository.save(testUser);
    }

    private void createAdditionalTestData() {
        // 계정 잠금 테스트용 사용자
        Set<String> userRoles = new HashSet<>();
        userRoles.add("USER");

        MemberEntity lockTestUser = MemberEntity.builder()
                .userId("locktest")
                .userName("Lock Test User")
                .password(passwordEncoder.encode("lockpass"))
                .roles(userRoles)
                .build();

        memberRepository.save(lockTestUser);
    }
}