package com.fordogs.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    SELLER("판매자"),
    BUYER("구매자");

    private final String description;

    @JsonCreator
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }

        throw GlobalErrorCode.badRequestException("유효하지 않은 회원 역할입니다.");
    }
}
