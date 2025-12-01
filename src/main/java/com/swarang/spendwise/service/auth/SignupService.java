package com.swarang.spendwise.service.auth;

import com.swarang.spendwise.dto.auth.SignupRequestDTO;
import com.swarang.spendwise.model.AuthUser;

public interface SignupService {
    AuthUser signupAndCreateProfile(SignupRequestDTO signupRequest);
}
