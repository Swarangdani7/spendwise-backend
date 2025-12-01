package com.swarang.spendwise.service.categorylimit;

import com.swarang.spendwise.dto.categorylimit.CategoryLimitRequestDTO;
import com.swarang.spendwise.dto.categorylimit.CategoryLimitResponseDTO;
import com.swarang.spendwise.model.CategoryLimit;
import com.swarang.spendwise.model.ProfileUser;

import java.util.List;

public interface CategoryLimitService {
    CategoryLimitResponseDTO createOrUpdateLimit(ProfileUser profileUser, CategoryLimitRequestDTO categoryLimitRequestDTO);

    List<CategoryLimitResponseDTO> getAllCategoryLimits(ProfileUser profileUser);
}
