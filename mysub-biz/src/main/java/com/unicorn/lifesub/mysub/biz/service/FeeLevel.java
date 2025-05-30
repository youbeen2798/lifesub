package com.unicorn.lifesub.mysub.biz.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeeLevel {

    LIKFER("liker"),
    COLLECTOR("collector"),
    ADDICT("addict");

    private final String feeLevel;
}
