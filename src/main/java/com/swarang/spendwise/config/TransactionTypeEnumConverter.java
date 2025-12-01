package com.swarang.spendwise.config;

import com.swarang.spendwise.model.TransactionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionTypeEnumConverter implements Converter<String, TransactionType> {
    @Override
    public TransactionType convert(String source) {
        return TransactionType.valueOf(source.trim().toUpperCase());
    }
}
