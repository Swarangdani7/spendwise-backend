package com.swarang.spendwise.service.auth.impl;

import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.model.RefreshToken;
import com.swarang.spendwise.repository.ProfileUserRepository;
import com.swarang.spendwise.repository.RefreshTokenRepository;
import com.swarang.spendwise.utils.JwtUtils;
import com.swarang.spendwise.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileUserRepository profileUserRepository;
    private final JwtUtils jwtUtils;
    private final Long REFRESH_EXPIRY_SECONDS = 2592000L; // 30 days

    @Override
    public String generateAccessToken(String username) {
        return jwtUtils.generateAccessToken(username);
    }

    @Override
    @Transactional
    public AuthUser getAuthCredentials(ProfileUser profileUser) {
        return profileUser.getAuthUser();
    }

    @Override
    @Transactional
    public ProfileUser getProfileCredentials(AuthUser authUser){
        return profileUserRepository.findByAuthUser(authUser)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for auth user"));
    }

    @Override
    @Transactional
    public String generateRefreshToken(AuthUser authUser) {
        ProfileUser profileUser = getProfileCredentials(authUser);
        String token = jwtUtils.generateRefreshToken();
        String tokenHash = jwtUtils.generateTokenHash(token);

        // delete any previous refresh token if its exists
        refreshTokenRepository.deleteByProfileUserId(profileUser.getId());
        refreshTokenRepository.flush();

        RefreshToken refreshToken = RefreshToken.builder()
                .profileUser(profileUser)
                .tokenHash(tokenHash)
                .expiryDate(LocalDateTime.now().plusSeconds(REFRESH_EXPIRY_SECONDS))
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    @Transactional
    public ProfileUser validateRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Refresh token missing");
        }

        String tokenHash = jwtUtils.generateTokenHash(token);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // delete refresh token if it is expired
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired, login again");
        }
        return refreshToken.getProfileUser();
    }
}
