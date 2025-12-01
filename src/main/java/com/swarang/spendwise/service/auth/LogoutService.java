package com.swarang.spendwise.service.auth;

import com.swarang.spendwise.model.ProfileUser;

public interface LogoutService {
    void invalidateUserSession(ProfileUser profileUser);
}
