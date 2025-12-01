package com.swarang.spendwise.repository;

import com.swarang.spendwise.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByProfileUserId(Long profileUserId);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
