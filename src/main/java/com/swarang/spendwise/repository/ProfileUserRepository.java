package com.swarang.spendwise.repository;

import com.swarang.spendwise.model.AuthUser;
import com.swarang.spendwise.model.ProfileUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileUserRepository extends JpaRepository<ProfileUser, Long> {
    Optional<ProfileUser> findByAuthUser(AuthUser authUser);
}
