package com.unicorn.lifesub.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenRefreshDTO {
	private String refreshToken;
}
