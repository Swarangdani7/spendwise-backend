package com.swarang.spendwise.security;

import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+username));

        return new AuthUserDetails(authUser);
    }
}
