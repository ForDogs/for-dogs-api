package com.fordogs.product.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fordogs.product.error.ProductErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    FOOD("식품"),
    CLOTHING("의류"),
    SNACK("간식"),
    TOY("장난감"),
    ACCESSORY("악세서리"),
    SUPPLEMENT("보충제");

    private final String description;

    @JsonCreator
    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw ProductErrorCode.INVALID_CATEGORY_NAME.toException();
    }

    public static Category validateCategoryName(String name) {
        if (name == null) {
            return null;
        }
        try {
            return Category.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw ProductErrorCode.INVALID_CATEGORY_NAME.toException();
        }
    }
}
