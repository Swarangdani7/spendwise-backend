package com.swarang.spendwise.service.auth;

import com.swarang.spendwise.dto.auth.LoginRequestDTO;
import com.swarang.spendwise.model.AuthUser;

public interface LoginService {
    AuthUser validateCredentials(LoginRequestDTO loginRequest);
}
