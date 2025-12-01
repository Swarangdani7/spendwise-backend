package com.swarang.spendwise.service.categorylimit.impl;

import com.swarang.spendwise.dto.categorylimit.CategoryLimitRequestDTO;
import com.swarang.spendwise.dto.categorylimit.CategoryLimitResponseDTO;
import com.swarang.spendwise.model.CategoryLimit;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.repository.CategoryLimitRepository;
import com.swarang.spendwise.service.categorylimit.CategoryLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryLimitServiceImpl implements CategoryLimitService {

    private final CategoryLimitRepository categoryLimitRepository;

    @Override
    @Transactional
    public List<CategoryLimitResponseDTO> getAllCategoryLimits(ProfileUser profileUser) {
        List<CategoryLimit> categoryLimitList = categoryLimitRepository.findAllByProfileUserId(profileUser.getId());
        return categoryLimitList.stream().map(this::generateResponse).toList();
    }

    @Override
    @Transactional
    public CategoryLimitResponseDTO createOrUpdateLimit(ProfileUser profileUser, CategoryLimitRequestDTO categoryLimitRequestDTO) {
        CategoryLimit categoryLimit = categoryLimitRepository.findByProfileUserIdAndCategoryType(profileUser.getId(), categoryLimitRequestDTO.categoryType())
                .orElse(new CategoryLimit());

        categoryLimit.setCategoryType(categoryLimitRequestDTO.categoryType());
        categoryLimit.setLimitAmount(categoryLimitRequestDTO.limitAmount());
        categoryLimit.setUpdatedAt(LocalDateTime.now());
        categoryLimit.setProfileUser(profileUser);

        CategoryLimit updated = categoryLimitRepository.save(categoryLimit);
        profileUser.getCategoryLimits().add(updated);

        return generateResponse(updated);
    }

    private CategoryLimitResponseDTO generateResponse(CategoryLimit categoryLimit) {
        return new CategoryLimitResponseDTO(
                categoryLimit.getId(),
                categoryLimit.getProfileUser().getId(),
                categoryLimit.getCategoryType(),
                categoryLimit.getLimitAmount(),
                categoryLimit.getCreatedAt(),
                categoryLimit.getUpdatedAt()
        );
    }
}
