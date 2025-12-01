package com.swarang.spendwise.service.dashboard;

import com.swarang.spendwise.dto.dashboard.DashboardResponseDTO;
import com.swarang.spendwise.model.ProfileUser;

public interface DashboardService {
    DashboardResponseDTO getDashboardDetails(ProfileUser profileUser);
}
