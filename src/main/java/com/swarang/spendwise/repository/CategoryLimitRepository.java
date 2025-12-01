package com.swarang.spendwise.repository;

import com.swarang.spendwise.model.CategoryLimit;
import com.swarang.spendwise.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryLimitRepository extends JpaRepository<CategoryLimit, Long> {
    List<CategoryLimit> findAllByProfileUserId(Long profileUserId);

    Optional<CategoryLimit> findByProfileUserIdAndCategoryType(Long profileUserId, CategoryType categoryType);
}
