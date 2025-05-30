package com.unicorn.lifesub.member.service;

import com.unicorn.lifesub.common.dto.JwtTokenDTO;
import com.unicorn.lifesub.member.dto.*;

public interface MemberService {
    JwtTokenDTO login(LoginRequest request);
    LogoutResponse logout(LogoutRequest request);
}
