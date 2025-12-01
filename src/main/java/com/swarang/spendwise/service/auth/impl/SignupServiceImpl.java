package com.swarang.spendwise.service.auth.impl;

import com.swarang.spendwise.dto.auth.SignupRequestDTO;
import com.swarang.spendwise.exception.EmailAlreadyExistsException;
import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import com.swarang.spendwise.repository.AuthUserRepository;
import com.swarang.spendwise.repository.ProfileUserRepository;
import com.swarang.spendwise.service.auth.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final AuthUserRepository authUserRepository;
    private final ProfileUserRepository profileUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthUser signupAndCreateProfile(SignupRequestDTO signupRequest) {
        if (authUserRepository.existsByEmail(signupRequest.email())) {
            throw new EmailAlreadyExistsException("User account already exists");
        }

        String pass = passwordEncoder.encode(signupRequest.password());
        AuthUser authUser = AuthUser.builder()
                .email(signupRequest.email())
                .password(pass)
                .build();

        authUserRepository.save(authUser);

        ProfileUser profileUser = ProfileUser.builder()
                .firstName(signupRequest.firstName())
                .lastName(signupRequest.lastName())
                .authUser(authUser)
                .updatedAt(LocalDateTime.now())
                .build();

        profileUserRepository.save(profileUser);
        return authUser;
    }
}
