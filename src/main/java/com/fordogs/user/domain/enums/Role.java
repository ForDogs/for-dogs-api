package com.fordogs.user.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    SELLER("판매자"),
    BUYER("구매자");

    private final String description;
}
