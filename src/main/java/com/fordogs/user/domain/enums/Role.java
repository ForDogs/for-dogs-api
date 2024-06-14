package com.fordogs.user.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fordogs.user.error.UserManagementErrorCode;
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
        throw UserManagementErrorCode.INVALID_USER_ROLE_NAME.toException();
    }
}
