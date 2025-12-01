package com.swarang.spendwise.service.auth;

import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;

public interface TokenService {
    String generateAccessToken(String username);

    ProfileUser getProfileCredentials(AuthUser authUser);

    String generateRefreshToken(AuthUser authUser);

    ProfileUser validateRefreshToken(String token);

    AuthUser getAuthCredentials(ProfileUser profileUser);
}
