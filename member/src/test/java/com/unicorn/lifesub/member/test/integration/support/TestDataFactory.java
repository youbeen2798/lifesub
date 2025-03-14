package com.unicorn.lifesub.member.test.integration.support;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.dto.LoginRequest;

import java.util.HashSet;
import java.util.Set;

public class TestDataFactory {
    public static final String TEST_USER_ID = "testUser";
    public static final String TEST_PASSWORD = "Password123!";
    public static final String TEST_USERNAME = "Test User";

    public static MemberEntity createMemberEntity() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        return MemberEntity.builder()
                .userId(TEST_USER_ID)
                .userName(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .roles(roles)
                .build();
    }

    public static LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUserId(TEST_USER_ID);
        request.setPassword(TEST_PASSWORD);
        return request;
    }
}