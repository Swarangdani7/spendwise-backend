package com.swarang.spendwise.controller.dashboard;

import com.swarang.spendwise.dto.dashboard.DashboardResponseDTO;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.service.auth.TokenService;
import com.swarang.spendwise.service.dashboard.DashboardService;
import com.swarang.spendwise.utils.AuthUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "Overall financial analytics")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthUtils authUtils;
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(){
        AuthUser authUser = authUtils.getAuthUser();
        ProfileUser profileUser = tokenService.getProfileCredentials(authUser);

        DashboardResponseDTO response = dashboardService.getDashboardDetails(profileUser);
        return  ResponseEntity.ok(response);
    }
}
