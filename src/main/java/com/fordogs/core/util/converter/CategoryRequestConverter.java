package com.fordogs.core.util.converter;

import com.fordogs.product.domain.enums.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryRequestConverter implements Converter<String, Category> {

    @Override
    public Category convert(String category) {
        return Category.validateCategoryName(category);
    }
}
