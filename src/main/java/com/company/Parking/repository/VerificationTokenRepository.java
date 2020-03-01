package com.company.Parking.repository;

import com.company.Parking.model.VerifiactionToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerifiactionToken, Long> {
}
