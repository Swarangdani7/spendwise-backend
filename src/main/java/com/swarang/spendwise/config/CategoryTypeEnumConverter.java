package com.swarang.spendwise.config;

import com.swarang.spendwise.model.CategoryType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryTypeEnumConverter implements Converter<String, CategoryType> {
    @Override
    public CategoryType convert(String source) {
        return CategoryType.valueOf(source.trim().toUpperCase());
    }
}
