package com.swarang.spendwise.utils;

import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.security.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public AuthUser getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userDetails.getAuthUser();
    }
}
