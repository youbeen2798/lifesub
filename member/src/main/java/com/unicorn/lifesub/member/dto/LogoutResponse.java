package com.unicorn.lifesub.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogoutResponse {
    private String message;
}
