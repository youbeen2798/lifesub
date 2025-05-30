package com.unicorn.lifesub.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(100, "Invalid input value"),
    INTERNAL_SERVER_ERROR(110, "Internal server error"),
    
    // Member
    MEMBER_NOT_FOUND(200, "Member not found"),
    INVALID_CREDENTIALS(210, "Invalid credentials"),
    TOKEN_EXPIRED(220, "Token expired"),
    SIGNATURE_VERIFICATION_EXCEPTION(230, "서명 검증 실패"),
    ALGORITHM_MISMATCH_EXCEPTION(240, "알고리즘 불일치"),
    INVALID_CLAIM_EXCEPTION(250, "유효하지 않은 클레임"),

    // Subscription
    SUBSCRIPTION_NOT_FOUND(300, "Subscription not found"),
    ALREADY_SUBSCRIBED(310, "Already subscribed to this service"),
    
    // Recommend
    NO_SPENDING_DATA(400, "No spending data found"),
    NO_RECOMMENDATION_DATA(410, "추천 구독 카테고리 없음"),

    // UnDefined
    UNDIFINED_ERROR(0, "정의되지 않은 에러");

    private final int status;
    private final String message;
}
