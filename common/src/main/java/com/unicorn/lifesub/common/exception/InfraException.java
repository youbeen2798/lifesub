package com.unicorn.lifesub.common.exception;

import lombok.Getter;

@Getter
public class InfraException extends RuntimeException {
    private final ErrorCode errorCode;

    public InfraException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
