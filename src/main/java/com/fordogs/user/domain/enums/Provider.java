package com.fordogs.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Provider {
    GOOGLE("구글"),
    KAKAO("카카오"),
    LOCAL("일반");

    private final String description;

    @JsonCreator
    public static Provider fromValue(String value) {
        for (Provider role : Provider.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }

        throw GlobalErrorCode.badRequestException("유효하지 않은 공급자 입니다.");
    }
}
