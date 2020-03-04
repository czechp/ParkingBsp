package com.company.Parking.repository;

import com.company.Parking.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    public boolean existsVerificationTokenByToken(String token);
    public Optional<VerificationToken> findVerificationTokenByToken(String token);
}
