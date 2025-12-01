package com.swarang.spendwise.controller.categorylimit;

import com.swarang.spendwise.dto.categorylimit.CategoryLimitRequestDTO;
import com.swarang.spendwise.dto.categorylimit.CategoryLimitResponseDTO;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.service.auth.TokenService;
import com.swarang.spendwise.service.categorylimit.CategoryLimitService;
import com.swarang.spendwise.utils.AuthUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Limits", description = "Budgeting limits per category")
@RestController
@RequestMapping("/category-limits")
@RequiredArgsConstructor
public class CategoryLimitController {

    private final CategoryLimitService categoryLimitService;
    private final AuthUtils authUtils;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<CategoryLimitResponseDTO> createOrUpdateLimit(@Valid @RequestBody CategoryLimitRequestDTO categoryLimitRequestDTO) {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        CategoryLimitResponseDTO response = categoryLimitService.createOrUpdateLimit(profileUser, categoryLimitRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryLimitResponseDTO>> getCategoryLimits() {
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        List<CategoryLimitResponseDTO> response = categoryLimitService.getAllCategoryLimits(profileUser);
        return ResponseEntity.ok(response);
    }
}
