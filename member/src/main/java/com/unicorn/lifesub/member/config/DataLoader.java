// File: lifesub/member/src/main/java/com/unicorn/lifesub/member/config/DataLoader.java
package com.unicorn.lifesub.member.config;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 기존 사용자 데이터가 없을 경우에만 초기 데이터 생성
        if (memberRepository.count() == 0) {
            Set<String> userRoles = new HashSet<>();
            userRoles.add("USER");
            String encodedPassword = passwordEncoder.encode("Passw0rd");

            IntStream.rangeClosed(1, 10).forEach(i -> {
                String userId = String.format("user%02d", i);
                MemberEntity member = MemberEntity.builder()
                        .userId(userId)
                        .userName("사용자" + i)
                        .password(encodedPassword)
                        .roles(userRoles)
                        .build();

                memberRepository.save(member);
            });
        }
    }
}
