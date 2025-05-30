package com.unicorn.lifesub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class JwtTokenDTO {
    private String accessToken;
    private String refreshToken;
}