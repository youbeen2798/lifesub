package com.unicorn.lifesub.member.service;

import com.unicorn.lifesub.common.exception.BusinessException;
import com.unicorn.lifesub.common.exception.ErrorCode;
import com.unicorn.lifesub.member.config.jwt.JwtTokenProvider;
import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.common.exception.InfraException;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional(readOnly = true)
    public JwtTokenDTO login(LoginRequest request) {
        MemberEntity member = memberRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new InfraException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 사용자의 권한 정보 생성
        Collection<? extends GrantedAuthority> authorities = member.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return jwtTokenProvider.createToken(member, authorities);
    }

    @Override
    @Transactional
    public LogoutResponse logout(LogoutRequest request) {
        // 실제 구현에서는 Redis 등을 사용하여 토큰 블랙리스트 관리
        return LogoutResponse.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();
    }
}
