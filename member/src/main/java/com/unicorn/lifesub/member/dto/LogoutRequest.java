package com.unicorn.lifesub.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;
}
