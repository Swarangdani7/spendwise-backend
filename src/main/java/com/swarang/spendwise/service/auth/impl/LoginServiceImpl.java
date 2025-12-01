package com.swarang.spendwise.service.auth.impl;

import com.swarang.spendwise.dto.auth.LoginRequestDTO;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.security.AuthUserDetails;
import com.swarang.spendwise.service.auth.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthUser validateCredentials(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        return authUserDetails.getAuthUser();
    }
}
