package com.swarang.spendwise.utils;

import com.swarang.spendwise.dto.transaction.TransactionFilterDTO;
import com.swarang.spendwise.model.CategoryType;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.model.Transaction;
import com.swarang.spendwise.model.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SpecificationUtils {

    public static Specification<Transaction> withFilter(ProfileUser profileUser, TransactionFilterDTO transactionFilterDTO) {
        Specification<Transaction> spec = Specification.unrestricted();

        if (transactionFilterDTO.type() != null) { spec = spec.and(hasType(transactionFilterDTO.type())); }
        if (transactionFilterDTO.category() != null) { spec = spec.and(hasCategory(transactionFilterDTO.category())); }
        if (transactionFilterDTO.start() != null) { spec = spec.and(hasStartDate(transactionFilterDTO.start())); }
        if (transactionFilterDTO.end() != null) { spec = spec.and(hasEndDate(transactionFilterDTO.end())); }
        if(profileUser != null){ spec = spec.and(belongsTo(profileUser));}

        return spec;
    }

    public static Specification<Transaction> hasCategory(CategoryType category) {
        return (root, query, cb) -> {
            return cb.equal(root.get("categoryType"), category);
        };
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> {
            return cb.equal(root.get("transactionType"), type);
        };
    }

    public static Specification<Transaction> hasStartDate(LocalDate startDate) {
        return (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
        };
    }

    public static Specification<Transaction> hasEndDate(LocalDate endDate) {
        return (root, query, cb) -> {
            return cb.lessThanOrEqualTo(root.get("transactionDate"), endDate);
        };
    }

    public static Specification<Transaction> belongsTo(ProfileUser profileUser) {
        return (root, query, cb) -> {
            return cb.equal(root.get("profileUser"), profileUser);
        };
    }
}
