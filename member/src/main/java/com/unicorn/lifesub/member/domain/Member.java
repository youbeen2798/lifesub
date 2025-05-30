package com.unicorn.lifesub.member.domain;

import lombok.Builder;
import lombok.Getter;
import java.util.Set;

@Getter
public class Member {
    private final String userId;
    private final String userName;
    private final String password;
    private final Set<String> roles;

    @Builder
    public Member(String userId, String userName, String password, Set<String> roles) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
}
