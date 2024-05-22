package com.fordogs.core.domian.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    FOOD("식품"),
    CLOTHING("의류"),
    SNACK("간식"),
    TOY("장난감"),
    ACCESSORY("악세서리"),
    SUPPLEMENT("보충제"),
    NONE("카테고리 없음");

    private final String description;
}
