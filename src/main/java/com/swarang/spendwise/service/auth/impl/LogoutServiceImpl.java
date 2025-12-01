package com.swarang.spendwise.service.auth.impl;

import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.repository.RefreshTokenRepository;
import com.swarang.spendwise.service.auth.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void invalidateUserSession(ProfileUser profileUser) {
        refreshTokenRepository.deleteByProfileUserId(profileUser.getId());
    }
}
